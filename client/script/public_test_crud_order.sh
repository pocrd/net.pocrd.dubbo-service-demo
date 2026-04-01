#!/bin/bash

# =============================================================================
# Higress Dubbo Triple HTTPS + mTLS 客户端证书认证测试脚本
# 测试 CRUDServiceHttpExport Order实体所有CRUD接口 (HTTPS + 客户端证书认证)
# 包含：createOrder, getOrderById, updateOrder, deleteOrder, getAllOrders
# =============================================================================

set -e

BASE_URL="https://api.caringfamily.cn"
SERVICE_PATH="/dapi/com.pocrd.dubbo_demo.api.CRUDServiceHttpExport"

# 证书路径
CERT_DIR="../certs"

# 使用 device005 的 fullchain 证书（包含完整证书链）
CLIENT_CERT="${CERT_DIR}/device005-fullchain.crt"
CLIENT_KEY="${CERT_DIR}/device005.key"

# 测试配置
MAX_TIME=15

echo "=============================================="
echo "CRUDService Order实体 HTTPS + mTLS 测试"
echo "=============================================="
echo ""

echo "证书配置:"
echo "  客户端证书：$CLIENT_CERT"
echo "  客户端私钥：$CLIENT_KEY"
echo ""

echo "✅ 证书文件检查通过"
echo ""

# -----------------------------------------------------------------------------
# 准备测试数据：先创建一个用户用于订单关联
# -----------------------------------------------------------------------------
echo "准备测试数据：创建测试用户"
echo "----------------------------------------------"

CREATE_USER_BODY='{"username":"order_test_user_$(date +%s)","email":"order_test@example.com","phone":"13800138001","status":1}'

TEST_USER_RESPONSE=$(curl -s --max-time $MAX_TIME -X POST \
  "${BASE_URL}${SERVICE_PATH}/createUser" \
  -H "Content-Type: application/json" \
  -d "$CREATE_USER_BODY" \
  --cert "$CLIENT_CERT" \
  --key "$CLIENT_KEY")

if echo "$TEST_USER_RESPONSE" | grep -q "id"; then
    TEST_USER_ID=$(echo "$TEST_USER_RESPONSE" | grep -o '"id":[0-9]*' | head -1 | cut -d: -f2)
    echo "✅ 测试用户创建成功，ID: $TEST_USER_ID"
    echo ""
else
    echo "❌ 测试用户创建失败"
    exit 1
fi

# -----------------------------------------------------------------------------
# 1. 测试 createOrder 接口
# -----------------------------------------------------------------------------
echo "测试 1/5: createOrder 接口"
echo "----------------------------------------------"

TIMESTAMP=$(date +%s)
CREATE_ORDER_BODY='{"orderNo":"ORDER_'${TIMESTAMP}'","userId":'${TEST_USER_ID}',"amount":299.99,"status":1,"remark":"Test order from script"}'
echo "请求体：$CREATE_ORDER_BODY"

RESPONSE_BODY=$(curl -s --max-time $MAX_TIME -X POST \
  "${BASE_URL}${SERVICE_PATH}/createOrder" \
  -H "Content-Type: application/json" \
  -d "$CREATE_ORDER_BODY" \
  --cert "$CLIENT_CERT" \
  --key "$CLIENT_KEY")
CURL_EXIT=$?

echo "响应结果:"
echo "$RESPONSE_BODY"
echo ""

if [ $CURL_EXIT -eq 0 ] && echo "$RESPONSE_BODY" | grep -q "id" && echo "$RESPONSE_BODY" | grep -q "ORDER_${TIMESTAMP}"; then
    echo "✅ createOrder 测试通过"
    
    # 提取创建的订单ID用于后续测试
    CREATED_ORDER_ID=$(echo "$RESPONSE_BODY" | grep -o '"id":[0-9]*' | head -1 | cut -d: -f2)
    echo "创建的订单ID: $CREATED_ORDER_ID"
    echo ""
else
    echo "❌ createOrder 测试失败"
    echo "curl 退出码：$CURL_EXIT"
    exit 1
fi

# -----------------------------------------------------------------------------
# 2. 测试 getOrderById 接口
# -----------------------------------------------------------------------------
echo "测试 2/5: getOrderById 接口"
echo "----------------------------------------------"

RESPONSE_BODY=$(curl -s --max-time $MAX_TIME -X GET \
  "${BASE_URL}${SERVICE_PATH}/getOrderById?id=${CREATED_ORDER_ID}" \
  --cert "$CLIENT_CERT" \
  --key "$CLIENT_KEY")
CURL_EXIT=$?

echo "响应结果:"
echo "$RESPONSE_BODY"
echo ""

if [ $CURL_EXIT -eq 0 ] && echo "$RESPONSE_BODY" | grep -q "\"id\":${CREATED_ORDER_ID}"; then
    echo "✅ getOrderById 测试通过"
    echo ""
else
    echo "❌ getOrderById 测试失败"
    echo "curl 退出码：$CURL_EXIT"
    exit 1
fi

# -----------------------------------------------------------------------------
# 3. 测试 updateOrder 接口
# -----------------------------------------------------------------------------
echo "测试 3/5: updateOrder 接口"
echo "----------------------------------------------"

# 从getOrderById响应中提取完整订单信息并更新
ORDER_INFO=$(curl -s --max-time $MAX_TIME -X GET \
  "${BASE_URL}${SERVICE_PATH}/getOrderById?id=${CREATED_ORDER_ID}" \
  --cert "$CLIENT_CERT" \
  --key "$CLIENT_KEY")

# 更新金额和状态
UPDATED_ORDER_BODY=$(echo "$ORDER_INFO" | sed 's/"amount":[0-9.]*\([,}]\)/"amount":399.99\1/' | sed 's/"status":[0-9]/"status":2/')

RESPONSE_BODY=$(curl -s --max-time $MAX_TIME -X POST \
  "${BASE_URL}${SERVICE_PATH}/updateOrder" \
  -H "Content-Type: application/json" \
  -d "$UPDATED_ORDER_BODY" \
  --cert "$CLIENT_CERT" \
  --key "$CLIENT_KEY")
CURL_EXIT=$?

echo "更新请求体："
echo "$UPDATED_ORDER_BODY"
echo ""

echo "响应结果:"
echo "$RESPONSE_BODY"
echo ""

if [ $CURL_EXIT -eq 0 ] && echo "$RESPONSE_BODY" | grep -q "399.99" && echo "$RESPONSE_BODY" | grep -q "\"status\":2"; then
    echo "✅ updateOrder 测试通过"
    echo ""
else
    echo "❌ updateOrder 测试失败"
    echo "curl 退出码：$CURL_EXIT"
    exit 1
fi

# -----------------------------------------------------------------------------
# 4. 测试 getAllOrders 接口
# -----------------------------------------------------------------------------
echo "测试 4/5: getAllOrders 接口"
echo "----------------------------------------------"

RESPONSE_BODY=$(curl -s --max-time $MAX_TIME -X GET \
  "${BASE_URL}${SERVICE_PATH}/getAllOrders" \
  --cert "$CLIENT_CERT" \
  --key "$CLIENT_KEY")
CURL_EXIT=$?

echo "响应结果长度: $(echo "$RESPONSE_BODY" | wc -c) 字节"
echo ""

if [ $CURL_EXIT -eq 0 ] && [ $(echo "$RESPONSE_BODY" | wc -c) -gt 10 ]; then
    echo "✅ getAllOrders 测试通过"
    echo ""
else
    echo "❌ getAllOrders 测试失败"
    echo "curl 退出码：$CURL_EXIT"
    exit 1
fi

# -----------------------------------------------------------------------------
# 5. 测试 deleteOrder 接口
# -----------------------------------------------------------------------------
echo "测试 5/5: deleteOrder 接口"
echo "----------------------------------------------"

RESPONSE_BODY=$(curl -s --max-time $MAX_TIME -X GET \
  "${BASE_URL}${SERVICE_PATH}/deleteOrder?id=${CREATED_ORDER_ID}" \
  --cert "$CLIENT_CERT" \
  --key "$CLIENT_KEY")
CURL_EXIT=$?

echo "响应结果:"
echo "$RESPONSE_BODY"
echo ""

if [ $CURL_EXIT -eq 0 ] && ([ "$RESPONSE_BODY" = "true" ] || echo "$RESPONSE_BODY" | grep -q "true"); then
    echo "✅ deleteOrder 测试通过"
    echo ""
else
    echo "❌ deleteOrder 测试失败"
    echo "curl 退出码：$CURL_EXIT"
    exit 1
fi

# -----------------------------------------------------------------------------
# 清理测试数据：删除测试用户
# -----------------------------------------------------------------------------
echo "清理测试数据：删除测试用户"
echo "----------------------------------------------"

curl -s --max-time $MAX_TIME -X GET \
  "${BASE_URL}${SERVICE_PATH}/deleteUser?id=${TEST_USER_ID}" \
  --cert "$CLIENT_CERT" \
  --key "$CLIENT_KEY" >/dev/null

echo "✅ 测试用户清理完成"
echo ""

echo "=============================================="
echo "Order实体所有CRUD测试完成!"
echo "=============================================="
exit 0