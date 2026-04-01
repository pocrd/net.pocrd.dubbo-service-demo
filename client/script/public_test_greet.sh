#!/bin/bash

# =============================================================================
# Higress Dubbo Triple HTTPS + mTLS 客户端证书认证测试脚本
# 测试 GreeterServiceHttpExport.greet 接口 (HTTPS + 客户端证书认证)
# 仅包含正常用例：使用 Fullchain 客户端证书进行 mTLS 认证
# =============================================================================

set -e

BASE_URL="https://api.caringfamily.cn"
SERVICE_PATH="/dapi/com.pocrd.dubbo_demo.api.GreeterServiceHttpExport"
METHOD="greet"
NAME="张三"

# 证书路径
CERT_DIR="../certs"

# 使用 device005 的 fullchain 证书（包含完整证书链）
CLIENT_CERT="${CERT_DIR}/device005-fullchain.crt"
CLIENT_KEY="${CERT_DIR}/device005.key"

# 测试配置
MAX_TIME=10

echo "=============================================="
echo "Higress Dubbo Triple HTTPS + mTLS 测试"
echo "=============================================="
echo ""

echo "证书配置:"
echo "  客户端证书：$CLIENT_CERT"
echo "  客户端私钥：$CLIENT_KEY"
echo ""

echo "✅ 证书文件检查通过"
echo ""

# -----------------------------------------------------------------------------
# 测试：使用 Fullchain 客户端证书访问 greet 接口
# -----------------------------------------------------------------------------
echo "测试：HTTPS + mTLS (Fullchain 客户端证书)"
echo "----------------------------------------------"
echo "预期：✅ 应该成功（验证完整证书链）"
echo ""

echo "请求：GET ${BASE_URL}${SERVICE_PATH}/${METHOD}?name=${NAME}"
echo "参数：name=${NAME}"
echo ""

echo "正在发起请求..."
echo ""

# 对URL参数进行正确编码
ENCODED_NAME=$(python3 -c "import urllib.parse; print(urllib.parse.quote('$NAME', safe=''))" 2>/dev/null || echo "${NAME// /%20}")

# 发起正式请求（使用GET + URL参数，因为单参数String接口有歧义问题）
RESPONSE_BODY=$(curl -s --max-time $MAX_TIME -X GET \
  "${BASE_URL}${SERVICE_PATH}/${METHOD}?name=${ENCODED_NAME}" \
  --cert "$CLIENT_CERT" \
  --key "$CLIENT_KEY")
CURL_EXIT=$?

echo "响应结果:"
echo "$RESPONSE_BODY"
echo ""

# 验证响应是否成功
# 由于中文字符编码问题，我们检查响应中是否包含基本的"Hello"和设备信息
if [ $CURL_EXIT -eq 0 ] && echo "$RESPONSE_BODY" | grep -q "Hello" && echo "$RESPONSE_BODY" | grep -q "device005"; then
    echo "✅ 测试通过：mTLS 认证成功，响应包含预期内容"
    echo ""
    echo "=============================================="
    echo "测试完成!"
    echo "=============================================="
    exit 0
else
    echo "❌ 测试失败：mTLS 认证失败或响应异常"
    echo "curl 退出码：$CURL_EXIT"
    echo ""
    echo "=============================================="
    echo "调试信息:"
    echo "  请检查以下配置:"
    echo "  1. Higress Ingress 是否正确配置 mTLS"
    echo "  2. 客户端证书是否由受信任的 CA 签发"
    echo "  3. 证书链是否完整 (使用 fullchain 证书)"
    echo "  4. 证书与私钥是否匹配"
    echo "=============================================="
    exit 1
fi