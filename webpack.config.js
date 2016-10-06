const webpack = require('webpack');

module.exports = {
  entry: './src/js/app.js',
  output: {
    path: './build/resources/main/static',
    filename: 'app.bundle.js'
  },
  module: {
    loaders: [{
      test: /\.jsx?$/,
      exclude: /node_modules/,
      loader: 'babel-loader'
    },{
      test: /\.css$/,
      loader: 'style!css'
    }]
  },
  devServer: {
    port: 9090,
    proxy: {
      '/*': {
        target: 'http://localhost:8080',
        secure: false,
        prependPath: false
      }
    },
    publicPath: 'http://localhost:9090/'
  },
  plugins: [
    new webpack.optimize.UglifyJsPlugin({
      compress: {
        warnings: false,
      },
      output: {
        comments: false,
      },
    }),
    new webpack.DefinePlugin({
      "process.env": { 
        NODE_ENV: JSON.stringify("production") 
      }
    }),
  ]
};
