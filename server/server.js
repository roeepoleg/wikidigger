var http = require("http");
var url = require("url");

function start() {
  function onRequest(request, response) {
    var pathname = url.parse(request.url).pathname;
    console.log("Request for " + pathname + " received.");
    response.writeHead(200, {"Content-Type": "text/plain"});
    var data = [{title:"title0",url:"http://url0.com",distance:0,image:"http://someiamge0.com"},
    {title:"title1",url:"http://url1.com",distance:3,image:"http://someiamge1.com"},
    {title:"title2",url:"http://url2.com",distance:6,image:"http://someiamge2.com"},
    {title:"title3",url:"http://url3.com",distance:9,image:"http://someiamge3.com"}];
    response.write(JSON.stringify(data));
    response.end();
  }

  http.createServer(onRequest).listen(8888);
  console.log("Server has started.");
}

exports.start = start;