var webpack = require('webpack');
var CopyWebpackPlugin = require('copy-webpack-plugin');

var path = require('path');
var DIST_DIR = path.resolve(__dirname, "build/resources/main/static");
var SRC_DIR = path.resolve(__dirname, "src");

var config = {
    entry: {
        app: ['babel-polyfill', SRC_DIR + "/app/index.js"],
        vendors: ['react',
            'react-dom',
            'react-redux',
            'react-router',
            'react-tap-event-plugin',
            'redux',
            'redux-actions',
            'redux-form',
            'redux-logger',
            'redux-saga',
            'jssha',
            'isomorphic-fetch',
            'moment',
            'nonce',
            'normalizr',
            'store2',
            'material-ui']
    },
    output: {
        path: DIST_DIR + "/app",
        filename: "bundle.js",
        publicPath: "/app/"
    },
    devtool: "source-map",
    module: {
        loaders: [
            {
                test: /\.jsx?$/,
                include: SRC_DIR,
                loaders: ["react-hot-loader/webpack", "babel?presets[]=react,presets[]=es2015,presets[]=stage-2"],
            }
        ],
        noParse: [/libs/]
    },
    plugins: [
        new webpack.optimize.CommonsChunkPlugin('vendors', 'vendors.js'),
        new webpack.DefinePlugin({
            "process.env": {
                NODE_ENV: JSON.stringify("production")
            }
        }),
        new CopyWebpackPlugin([
            {from: SRC_DIR + "/app/libs", to: DIST_DIR + "/app/libs"},
            {from: SRC_DIR + "/app/css", to: DIST_DIR + "/app/css"},
        ])
    ]
};

module.exports = config;