const { defineConfig } = require('@vue/cli-service');
module.exports = defineConfig({
  transpileDependencies: ['vuetify'],
  devServer: {
    client: {
      overlay: false,
    },
    proxy: {
      '/api': {
        target:
          'http://ec2-3-37-84-38.ap-northeast-2.compute.amazonaws.com:8080',
        changeOrigin: true,
      },
    },
  },
});
