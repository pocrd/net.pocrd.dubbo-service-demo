#!/bin/bash

# =============================================================================
# Higress Dubbo Triple HTTPS + mTLS 客户端证书认证测试脚本
# 测试 CRUDServiceHttpExport User实体所有CRUD接口 (HTTPS + 客户端证书认证)
# 包含：createUser, getUserById, updateUser, deleteUser, getAllUsers
# =============================================================================

set -e

BASE_URL="https://api.caringfamily.cn"
SERVICE_PATH="/dapi/com.pocrd.service_demo.api.CRUDServiceHttpExport"

# 证书路径
CERT_DIR="../certs"

# 使用 device005 的 fullchain 证书（包含完整证书链）
CLIENT_CERT="${CERT_DIR}/device005-fullchain.crt"
CLIENT_KEY="${CERT_DIR}/device005.key"

# 测试配置
MAX_TIME=15

echo "=============================================="
echo "CRUDService User实体 HTTPS + mTLS 测试"
echo "=============================================="
echo ""

echo "证书配置:"
echo "  客户端证书：$CLIENT_CERT"
echo "  客户端私钥：$CLIENT_KEY"
echo ""

echo "✅ 证书文件检查通过"
echo ""

# -----------------------------------------------------------------------------
# 1. 测试 createUser 接口
# -----------------------------------------------------------------------------
echo "测试 1/5: createUser 接口"
echo "----------------------------------------------"

CREATE_USER_BODY='{"username":"test_user_$(date +%s)","email":"test@example.com","phone":"13800138000","status":1}'
echo "请求体：$CREATE_USER_BODY"

RESPONSE_BODY=$(curl -s --max-time $MAX_TIME -X POST \
  "${BASE_URL}${SERVICE_PATH}/createUser" \
  -H "Content-Type: application/json" \
  -d "$CREATE_USER_BODY" \
  --cert "$CLIENT_CERT" \
  --key "$CLIENT_KEY")
CURL_EXIT=$?

echo "响应结果:"
echo "$RESPONSE_BODY"
echo ""

if [ $CURL_EXIT -eq 0 ] && echo "$RESPONSE_BODY" | grep -q "id" && echo "$RESPONSE_BODY" | grep -q "test_user"; then
    echo "✅ createUser 测试通过"
    
    # 提取创建的用户ID用于后续测试
    CREATED_USER_ID=$(echo "$RESPONSE_BODY" | grep -o '"id":[0-9]*' | head -1 | cut -d: -f2)
    echo "创建的用户ID: $CREATED_USER_ID"
    echo ""
else
    echo "❌ createUser 测试失败"
    echo "curl 退出码：$CURL_EXIT"
    exit 1
fi

# -----------------------------------------------------------------------------
# 2. 测试 getUserById 接口
# -----------------------------------------------------------------------------
echo "测试 2/5: getUserById 接口"
echo "----------------------------------------------"

RESPONSE_BODY=$(curl -s --max-time $MAX_TIME -X GET \
  "${BASE_URL}${SERVICE_PATH}/getUserById?id=${CREATED_USER_ID}" \
  --cert "$CLIENT_CERT" \
  --key "$CLIENT_KEY")
CURL_EXIT=$?

echo "响应结果:"
echo "$RESPONSE_BODY"
echo ""

if [ $CURL_EXIT -eq 0 ] && echo "$RESPONSE_BODY" | grep -q "\"id\":${CREATED_USER_ID}"; then
    echo "✅ getUserById 测试通过"
    echo ""
else
    echo "❌ getUserById 测试失败"
    echo "curl 退出码：$CURL_EXIT"
    exit 1
fi

# -----------------------------------------------------------------------------
# 3. 测试 updateUser 接口
# -----------------------------------------------------------------------------
echo "测试 3/5: updateUser 接口"
echo "----------------------------------------------"

# 从getUserById响应中提取完整用户信息并更新
USER_INFO=$(curl -s --max-time $MAX_TIME -X GET \
  "${BASE_URL}${SERVICE_PATH}/getUserById?id=${CREATED_USER_ID}" \
  --cert "$CLIENT_CERT" \
  --key "$CLIENT_KEY")

# 更新邮箱和状态
UPDATED_USER_BODY=$(echo "$USER_INFO" | sed 's/"email":"[^"]*"/"email":"updated_test@example.com"/' | sed 's/"status":[0-9]/"status":2/')

RESPONSE_BODY=$(curl -s --max-time $MAX_TIME -X POST \
  "${BASE_URL}${SERVICE_PATH}/updateUser" \
  -H "Content-Type: application/json" \
  -d "$UPDATED_USER_BODY" \
  --cert "$CLIENT_CERT" \
  --key "$CLIENT_KEY")
CURL_EXIT=$?

echo "更新请求体："
echo "$UPDATED_USER_BODY"
echo ""

echo "响应结果:"
echo "$RESPONSE_BODY"
echo ""

if [ $CURL_EXIT -eq 0 ] && echo "$RESPONSE_BODY" | grep -q "updated_test@example.com" && echo "$RESPONSE_BODY" | grep -q "\"status\":2"; then
    echo "✅ updateUser 测试通过"
    echo ""
else
    echo "❌ updateUser 测试失败"
    echo "curl 退出码：$CURL_EXIT"
    exit 1
fi

# -----------------------------------------------------------------------------
# 4. 测试 getAllUsers 接口
# -----------------------------------------------------------------------------
echo "测试 4/5: getAllUsers 接口"
echo "----------------------------------------------"

RESPONSE_BODY=$(curl -s --max-time $MAX_TIME -X GET \
  "${BASE_URL}${SERVICE_PATH}/getAllUsers" \
  --cert "$CLIENT_CERT" \
  --key "$CLIENT_KEY")
CURL_EXIT=$?

echo "响应结果长度: $(echo "$RESPONSE_BODY" | wc -c) 字节"
echo ""

if [ $CURL_EXIT -eq 0 ] && [ $(echo "$RESPONSE_BODY" | wc -c) -gt 10 ]; then
    echo "✅ getAllUsers 测试通过"
    echo ""
else
    echo "❌ getAllUsers 测试失败"
    echo "curl 退出码：$CURL_EXIT"
    exit 1
fi

# -----------------------------------------------------------------------------
# 5. 测试 deleteUser 接口
# -----------------------------------------------------------------------------
echo "测试 5/5: deleteUser 接口"
echo "----------------------------------------------"

RESPONSE_BODY=$(curl -s --max-time $MAX_TIME -X GET \
  "${BASE_URL}${SERVICE_PATH}/deleteUser?id=${CREATED_USER_ID}" \
  --cert "$CLIENT_CERT" \
  --key "$CLIENT_KEY")
CURL_EXIT=$?

echo "响应结果:"
echo "$RESPONSE_BODY"
echo ""

if [ $CURL_EXIT -eq 0 ] && ([ "$RESPONSE_BODY" = "true" ] || echo "$RESPONSE_BODY" | grep -q "true"); then
    echo "✅ deleteUser 测试通过"
    echo ""
else
    echo "❌ deleteUser 测试失败"
    echo "curl 退出码：$CURL_EXIT"
    exit 1
fi

echo "=============================================="
echo "User实体所有CRUD测试完成!"
echo "=============================================="
exit 0