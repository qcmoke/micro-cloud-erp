非对称加密（加密和解密不是使用同一个秘钥）

一、生成密钥对(包含有私钥和公钥)，并存在秘钥库文件(keystore文件)中，秘钥库文件是一个二进制文件，包含有多个证书条目（可使用java自带的工具keytool生成）
#storepass是密钥库密码;keypass是alias对应的证书密码（默认与storepass相同）
$ keytool -genkeypair -alias qcmoke -storepass 123456password -keypass 123456secret -keystore ./qcmoke.keystore -keyalg RSA -dname "CN=Web Server,OU=China,O=www.qcmoke.site,L=Beijing,S=Beijing,C=China"
#密钥库口令：123456password

二、查看密钥库文件信息
$ keytool -list -keystore ./qcmoke.keystore -v
#密钥库口令：123456password

四、查看证书信息
$ keytool -printcert -file ./qcmoke.crt

五、从秘钥库中导出证书
$ keytool -export -keystore ./qcmoke.keystore -alias qcmoke -file ./qcmoke.crt -rfc
#密钥库口令：123456password

六、查看公钥
$ keytool -list -rfc --keystore ./qcmoke.keystore | openssl x509 -inform pem -pubkey
#密钥库口令：123456password

七、copy如下内容到public.key即可。
-----BEGIN PUBLIC KEY-----
MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAwlJSUTtDeUML9LYp6X/r
5/3LxdKaLLwpTPr+OHPYjRnYCdYXpG/CN13A+fubmX0Hs8zLfSN7Y+Ao93g1VDou
Y6FPcrDqoQ71AuH7lZG2eqHyOaOYKgfUSAG8N6I2OWZ7z1t1dRSBJbioP0xAic+O
8O31Mej23l64yuV9GogWRu0XjKam5fLumWLMmGGlMVYYZHUgX1AFbZQzgrophmug
dm5EOQWtok6Y3tS2/vbjVh/x/MNFMWsUVFjMi9t8UUU1v2S7twL/oPDXIc+lqoPX
UqqGXX5EZ0zn+yzwx2Ge53vTTut64wgrZCYuothNU0ULkfSVoW8SjJUjSwPJAKDI
0wIDAQAB
-----END PUBLIC KEY-----