module.exports = {
  title: 'M-ERP',
  // Spring Boot Admin控制台地址
  springBootAdminUrl: 'http://localhost:8401',
  // Swagger UI 地址
  swaggerUrl: 'http://localhost:8301/system/swagger-ui.html',
  // 获取令牌时，请求头信息(Basic Base64.encode(client_id:client_secret))
  authorizationValue: 'Basic d2ViQXBwOjEyMzQ1Ng==',
  // 社交登录后台地址
  socialLoginUrl: 'http://apicloud.febs.cc:8301/auth/social/login'
}
