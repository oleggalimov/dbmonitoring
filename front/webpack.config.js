const path = require ('path');

module.exports = {
    mode:"development",
    entry :"./src/main/js/app/index.tsx",
    output :{
        path: path.resolve (__dirname, "src/main/webapp/static/js"),
        filename: "bundle.js"

    },
    module: {
        rules: [
            {
                test: /\.(ts|tsx)$/,
                loader: "ts-loader"
            },
            {
                test:/\.css$/,
                use: [
                    'style-loader',
                    'css-loader'
                ]
                // loader: 'style-loader!css-loader'
            }
        ]
    },
    resolve: {
        extensions: [".ts", ".tsx", ".js", ".jsx",".json",".css"]
    },
    devServer: {
        contentBase: path.join (__dirname, "./src/main/webapp/static"),
        port:9000,
        //hot:true,
        https:false,
        historyApiFallback:true
    }
}