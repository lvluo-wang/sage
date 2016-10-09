var webpack = require('webpack');

var path = require('path');
var DIST_DIR = path.resolve(__dirname, "build/resources/main/static");
var SRC_DIR = path.resolve(__dirname, "src");

var config = {
    entry: {
        app: SRC_DIR + "/app/index.js",
        vendors: ['react',
            'react-dom',
            'react-redux',
            'react-router',
            'redux',
            'redux-actions',
            'redux-form',
            'redux-logger']
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
        // new webpack.optimize.UglifyJsPlugin({
        //     compress: {
        //         warnings: false
        //     }
        // })
    ]
};

module.exports = config;