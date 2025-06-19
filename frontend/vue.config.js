const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true
})
 

module.exports = {
  // npm run build를 실행하면 해당 디렉토리 안에 빌딩된다.
  // 프로젝트 종료 후 빌드하여 백엔드 서버만 릴리즈하면 된다.
  outputDir: '../src/main/resources/static', // 빌드 타겟 디렉토리
  devServer: {
    proxy: {
      '/api': {
        // '/api' 로 들어오면 포트 8000(스프링 서버)로 보낸다
        target: 'http://localhost:8000',
        changeOrigin: true // cross origin 허용
      }
    }
  }
}