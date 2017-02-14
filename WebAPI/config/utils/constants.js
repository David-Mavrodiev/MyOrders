/*globals module*/
let connString;

if (process.env.NODE_ENV === 'production') {
    connString = "...";
} else {
    connString = "mongodb://localhost/myorders";
}

module.exports = {
    connectionString: connString
};