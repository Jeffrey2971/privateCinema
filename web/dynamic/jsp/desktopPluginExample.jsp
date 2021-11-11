<%@ page contentType="text/html;charset=UTF-8" isELIgnored="false" %>

<html>
<head>
    <meta charset="utf-8"/>
    <meta name="viewport"
          content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no"/>
    <title>${title}</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/mui-player.min.css">
    <script src="${pageContext.request.contextPath}/static/js/mui-player.min.js"></script>
    <script src="${pageContext.request.contextPath}/static/js/mui-player-desktop-plugin.min.js"></script>
    <script src="${pageContext.request.contextPath}/static/js/jquery-1.7.2.min.js"></script>
    <script>
        var playerConfig = {
            container: '#mui-player',
            title: '${title}',
            src: '${videoSrc}',
            poster: '${videoPoster}',
            autoplay: true,
            videoAttribute: [
                {attrKey: 'webkit-playsinline', attrValue: ''},
                {attrKey: 'playsinline', attrValue: ''},
                {attrKey: 'x5-playsinline', attrValue: ''},
                {attrKey: 't7-video-player-type', attrValue: 'inline'},
                {attrKey: 'x5-video-player-type', attrValue: 'h5-page'},
                {attrKey: 'x-webkit-airplay', attrValue: 'allow'},
                {attrKey: 'controlslist', attrValue: 'nodownload'},
            ],
        }
    </script>
</head>

<body>
<h3 style="text-align: center;">${title}</h3>
<div id="mui-player" style="max-width: 650px;margin: auto"></div>
</body>

<script>
    var config = window.playerConfig;
    config.plugins = [
        typeof MuiPlayerDesktopPlugin == 'function' ? new MuiPlayerDesktopPlugin({
            thumbnails:{ // 缩略图配置
                preview:${previewList},
                tile:[10,10],
                scale:[160,90],
            },
        }) : {},
    ]

    console.log(config);
    var mp = new MuiPlayer(config);
</script>
</html>