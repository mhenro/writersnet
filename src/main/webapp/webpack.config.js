const path = require('path');
const webpack = require('webpack');

module.exports = {
    entry: './site/js/app.jsx',
    output: {
        path: path.resolve(__dirname + '/site/', 'bundle'),
        filename: 'app.bundle.js'
    },
    module: {
        loaders: [
            {
                test: /\.jsx?$/,
                loader: 'babel-loader',
                exclude: /node_modules/,
                options: {
                    presets: ['es2015', 'react', 'stage-2']
                }
            },
            {
                test: /\.css$/,
                loader: 'style-loader!css-loader'
            }
        ]
    }
};