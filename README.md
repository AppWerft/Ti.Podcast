Ti.Podcast
==========

This is a simple podcast downloader. Input url, output json-object.

Usage
-----

```java
require("de.appwerft.podcast").loadPodcast({
    url : "http://www.deutschlandfunk.de/podcast-essay-und-diskurs.1185.de.podcast.xml",
    timeout : 10000,
    userAgent : "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.10; rv:46.0) Gecko/20100101 Firefox/46.0"
},function(_e) {
    console.log(_e.channel.title);
    console.log(_e.items[0].description);
});
```

