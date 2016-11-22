package com.blazers.jandan.util;

import android.util.Log;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by blazers on 2016/11/18.
 */
public class HtmlImgReplaceUtilTest {

    private static final String HTML_1 = "\n" +
            "<!DOCTYPE html>\n" +
            "<!--[if IE 6]><html class=\"ie lt-ie8\"><![endif]-->\n" +
            "<!--[if IE 7]><html class=\"ie lt-ie8\"><![endif]-->\n" +
            "<!--[if IE 8]><html class=\"ie ie8\"><![endif]-->\n" +
            "<!--[if IE 9]><html class=\"ie ie9\"><![endif]-->\n" +
            "<!--[if !IE]><!--> <html> <!--<![endif]-->\n" +
            "\n" +
            "<head>\n" +
            "  <meta charset=\"utf-8\">\n" +
            "  <meta http-equiv=\"X-UA-Compatible\" content=\"IE=Edge\">\n" +
            "<script type=\"text/javascript\">window.NREUM||(NREUM={});NREUM.info={\"beacon\":\"bam.nr-data.net\",\"errorBeacon\":\"bam.nr-data.net\",\"licenseKey\":\"1255494d3a\",\"applicationID\":\"15702971\",\"transactionName\":\"e1daR0JWVV9RER9ZWkxdRxxDUVZE\",\"queueTime\":0,\"applicationTime\":125,\"agent\":\"\"}</script>\n" +
            "<script type=\"text/javascript\">window.NREUM||(NREUM={}),__nr_require=function(e,t,n){function r(n){if(!t[n]){var o=t[n]={exports:{}};e[n][0].call(o.exports,function(t){var o=e[n][1][t];return r(o||t)},o,o.exports)}return t[n].exports}if(\"function\"==typeof __nr_require)return __nr_require;for(var o=0;o<n.length;o++)r(n[o]);return r}({1:[function(e,t,n){function r(){}function o(e,t,n){return function(){return i(e,[(new Date).getTime()].concat(u(arguments)),t?null:this,n),t?void 0:this}}var i=e(\"handle\"),a=e(2),u=e(3),c=e(\"ee\").get(\"tracer\"),f=NREUM;\"undefined\"==typeof window.newrelic&&(newrelic=f);var s=[\"setPageViewName\",\"setCustomAttribute\",\"setErrorHandler\",\"finished\",\"addToTrace\",\"inlineHit\"],l=\"api-\",p=l+\"ixn-\";a(s,function(e,t){f[t]=o(l+t,!0,\"api\")}),f.addPageAction=o(l+\"addPageAction\",!0),f.setCurrentRouteName=o(l+\"routeName\",!0),t.exports=newrelic,f.interaction=function(){return(new r).get()};var d=r.prototype={createTracer:function(e,t){var n={},r=this,o=\"function\"==typeof t;return i(p+\"tracer\",[Date.now(),e,n],r),function(){if(c.emit((o?\"\":\"no-\")+\"fn-start\",[Date.now(),r,o],n),o)try{return t.apply(this,arguments)}finally{c.emit(\"fn-end\",[Date.now()],n)}}}};a(\"setName,setAttribute,save,ignore,onEnd,getContext,end,get\".split(\",\"),function(e,t){d[t]=o(p+t)}),newrelic.noticeError=function(e){\"string\"==typeof e&&(e=new Error(e)),i(\"err\",[e,(new Date).getTime()])}},{}],2:[function(e,t,n){function r(e,t){var n=[],r=\"\",i=0;for(r in e)o.call(e,r)&&(n[i]=t(r,e[r]),i+=1);return n}var o=Object.prototype.hasOwnProperty;t.exports=r},{}],3:[function(e,t,n){function r(e,t,n){t||(t=0),\"undefined\"==typeof n&&(n=e?e.length:0);for(var r=-1,o=n-t||0,i=Array(o<0?0:o);++r<o;)i[r]=e[t+r];return i}t.exports=r},{}],ee:[function(e,t,n){function r(){}function o(e){function t(e){return e&&e instanceof r?e:e?c(e,u,i):i()}function n(n,r,o){if(!p.aborted){e&&e(n,r,o);for(var i=t(o),a=v(n),u=a.length,c=0;c<u;c++)a[c].apply(i,r);var f=s[w[n]];return f&&f.push([y,n,r,i]),i}}function d(e,t){b[e]=v(e).concat(t)}function v(e){return b[e]||[]}function g(e){return l[e]=l[e]||o(n)}function m(e,t){f(e,function(e,n){t=t||\"feature\",w[n]=t,t in s||(s[t]=[])})}var b={},w={},y={on:d,emit:n,get:g,listeners:v,context:t,buffer:m,abort:a,aborted:!1};return y}function i(){return new r}function a(){(s.api||s.feature)&&(p.aborted=!0,s=p.backlog={})}var u=\"nr@context\",c=e(\"gos\"),f=e(2),s={},l={},p=t.exports=o();p.backlog=s},{}],gos:[function(e,t,n){function r(e,t,n){if(o.call(e,t))return e[t];var r=n();if(Object.defineProperty&&Object.keys)try{return Object.defineProperty(e,t,{value:r,writable:!0,enumerable:!1}),r}catch(i){}return e[t]=r,r}var o=Object.prototype.hasOwnProperty;t.exports=r},{}],handle:[function(e,t,n){function r(e,t,n,r){o.buffer([e],r),o.emit(e,t,n)}var o=e(\"ee\").get(\"handle\");t.exports=r,r.ee=o},{}],id:[function(e,t,n){function r(e){var t=typeof e;return!e||\"object\"!==t&&\"function\"!==t?-1:e===window?0:a(e,i,function(){return o++})}var o=1,i=\"nr@id\",a=e(\"gos\");t.exports=r},{}],loader:[function(e,t,n){function r(){if(!h++){var e=y.info=NREUM.info,t=l.getElementsByTagName(\"script\")[0];if(setTimeout(f.abort,3e4),!(e&&e.licenseKey&&e.applicationID&&t))return f.abort();c(b,function(t,n){e[t]||(e[t]=n)}),u(\"mark\",[\"onload\",a()],null,\"api\");var n=l.createElement(\"script\");n.src=\"https://\"+e.agent,t.parentNode.insertBefore(n,t)}}function o(){\"complete\"===l.readyState&&i()}function i(){u(\"mark\",[\"domContent\",a()],null,\"api\")}function a(){return(new Date).getTime()}var u=e(\"handle\"),c=e(2),f=e(\"ee\"),s=window,l=s.document,p=\"addEventListener\",d=\"attachEvent\",v=s.XMLHttpRequest,g=v&&v.prototype;NREUM.o={ST:setTimeout,CT:clearTimeout,XHR:v,REQ:s.Request,EV:s.Event,PR:s.Promise,MO:s.MutationObserver},e(1);var m=\"\"+location,b={beacon:\"bam.nr-data.net\",errorBeacon:\"bam.nr-data.net\",agent:\"js-agent.newrelic.com/nr-998.min.js\"},w=v&&g&&g[p]&&!/CriOS/.test(navigator.userAgent),y=t.exports={offset:a(),origin:m,features:{},xhrWrappable:w};l[p]?(l[p](\"DOMContentLoaded\",i,!1),s[p](\"load\",r,!1)):(l[d](\"onreadystatechange\",o),s[d](\"onload\",r)),u(\"mark\",[\"firstbyte\",a()],null,\"api\");var h=0},{}]},{},[\"loader\"]);</script>\n" +
            "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0,user-scalable=no\">\n" +
            "  <meta http-equiv=\"Cache-Control\" content=\"no-siteapp\" />\n" +
            "  <meta http-equiv=\"Cache-Control\" content=\"no-transform\" />\n" +
            "    <meta name=\"description\"  content=\"本篇教程翻译自GoogleI/O2015中关于测试的codelab，掌握科学上网的同学请点击这里阅读：UnitandUITestinginAndroidStudio。能力有限，如有翻译错误，请批评指正。如需转载，请注明出处。Github下载测试源码目录...\">\n" +
            "  <meta property=\"wb:webmaster\" content=\"294ec9de89e7fadb\" />\n" +
            "  <meta property=\"qc:admins\" content=\"104102651453316562112116375\" />\n" +
            "  <meta property=\"qc:admins\" content=\"11635613706305617\" />\n" +
            "  <meta property=\"qc:admins\" content=\"1163561616621163056375\" />\n" +
            "  <meta name=\"google-site-verification\" content=\"cV4-qkUJZR6gmFeajx_UyPe47GW9vY6cnCrYtCHYNh4\" />\n" +
            "  <meta name=\"google-site-verification\" content=\"HF7lfF8YEGs1qtCE-kPml8Z469e2RHhGajy6JPVy5XI\" />\n" +
            "  <meta http-equiv=\"mobile-agent\" content=\"format=html5; url=http://www.jianshu.com/p/03118c11c199\">\n" +
            "    <!--  Meta for Smart App Banner -->\n" +
            "  <meta name=\"apple-itunes-app\" content=\"app-id=888237539, app-argument=jianshu://notes/1539608\">\n" +
            "  <!-- End -->\n" +
            "\n" +
            "  <!--  Meta for Twitter Card -->\n" +
            "  <meta content=\"summary\" property=\"twitter:card\">\n" +
            "  <meta content=\"@jianshucom\" property=\"twitter:site\">\n" +
            "  <meta content=\"在Android Studio中进行单元测试和UI测试\" property=\"twitter:title\">\n" +
            "  <meta content=\"本篇教程翻译自GoogleI/O2015中关于测试的codelab，掌握科学上网的同学请点击这里阅读：UnitandUITestinginAndroidStudio。能力有限，如有翻译错误，请批评指正。如需转载，请注明出处。Github下载测试源码目录...\" property=\"twitter:description\">\n" +
            "  <meta content=\"http://www.jianshu.com/p/03118c11c199\" property=\"twitter:url\">\n" +
            "  <!-- End -->\n" +
            "\n" +
            "  <!--  Meta for OpenGraph -->\n" +
            "  <meta property=\"fb:app_id\" content=\"865829053512461\">\n" +
            "  <meta property=\"og:site_name\" content=\"简书\">\n" +
            "  <meta property=\"og:title\" content=\"在Android Studio中进行单元测试和UI测试\">\n" +
            "  <meta property=\"og:type\" content=\"article\">\n" +
            "  <meta property=\"og:url\" content=\"http://www.jianshu.com/p/03118c11c199\">\n" +
            "  <meta property=\"og:description\" content=\"本篇教程翻译自GoogleI/O2015中关于测试的codelab，掌握科学上网的同学请点击这里阅读：UnitandUITestinginAndroidStudio。能力有限，如有翻译错误，请批...\">\n" +
            "  <!-- End -->\n" +
            "\n" +
            "  <!--  Meta for Facebook Applinks -->\n" +
            "  <meta property=\"al:ios:url\" content=\"jianshu://notes/1539608\" />\n" +
            "  <meta property=\"al:ios:app_store_id\" content=\"888237539\" />\n" +
            "  <meta property=\"al:ios:app_name\" content=\"简书\" />\n" +
            "\n" +
            "  <meta property=\"al:android:url\" content=\"jianshu://notes/1539608\" />\n" +
            "  <meta property=\"al:android:package\" content=\"com.jianshu.haruki\" />\n" +
            "  <meta property=\"al:android:app_name\" content=\"简书\" />\n" +
            "  <!-- End -->\n" +
            "\n" +
            "  <title>在Android Studio中进行单元测试和UI测试 - 简书</title>\n" +
            "  <meta name=\"csrf-param\" content=\"authenticity_token\" />\n" +
            "<meta name=\"csrf-token\" content=\"k1YaAJ64uE5WeLLiuZHXlcwD/8dNLokPMEyt2nfDo5+7C0p1BaYWspqJBlutyiON/aviFg2gNEj3E9cdD4XPtQ==\" />\n" +
            "  <!--[if lte IE 8]><script src=\"http://html5shiv.googlecode.com/svn/trunk/html5.js\"></script><![endif]-->\n" +
            "  <link rel=\"stylesheet\" media=\"all\" href=\"http://cdn-qn0.jianshu.io/assets/base-00828532d00225531ad20ff45bf311ea.css\" />\n" +
            "\n" +
            "    <link rel=\"stylesheet\" media=\"all\" href=\"http://cdn-qn0.jianshu.io/assets/reading-note-a4d1e4bd26d83f631d58a76778118af6.css\" />\n" +
            "  <link rel=\"stylesheet\" media=\"all\" href=\"http://cdn-qn0.jianshu.io/assets/base-read-mode-5f4051bc94fc595e8bc00821aae6c3e1.css\" />\n" +
            "  <script src=\"http://cdn-qn0.jianshu.io/assets/modernizr-613ea63b5aa2f0e2a1946e9c28c8eedb.js\"></script>\n" +
            "  <!-- Le HTML5 shim, for IE6-8 support of HTML elements -->\n" +
            "  <!--[if IE 8]><link rel=\"stylesheet\" media=\"all\" href=\"http://cdn-qn0.jianshu.io/assets/scaffolding/for_ie-7f1c477ffedc13c11315103e8787dc6c.css\" /><![endif]-->\n" +
            "  <!--[if lt IE 9]><link rel=\"stylesheet\" media=\"all\" href=\"http://cdn-qn0.jianshu.io/assets/scaffolding/for_ie-7f1c477ffedc13c11315103e8787dc6c.css\" /><![endif]-->\n" +
            "  <link href=\"http://baijii-common.b0.upaiyun.com/icons/favicon.ico\" rel=\"icon\">\n" +
            "      <link rel=\"apple-touch-icon-precomposed\" href=\"http://cdn-qn0.jianshu.io/assets/apple-touch-icons/57-b426758a1fcfb30486f20fd073c3b8ec.png\" sizes=\"57x57\" />\n" +
            "      <link rel=\"apple-touch-icon-precomposed\" href=\"http://cdn-qn0.jianshu.io/assets/apple-touch-icons/72-feca4b183b9d29fd188665785dc7a7f1.png\" sizes=\"72x72\" />\n" +
            "      <link rel=\"apple-touch-icon-precomposed\" href=\"http://cdn-qn0.jianshu.io/assets/apple-touch-icons/76-ba757f1ad3421192ce7192170393d2b0.png\" sizes=\"76x76\" />\n" +
            "      <link rel=\"apple-touch-icon-precomposed\" href=\"http://cdn-qn0.jianshu.io/assets/apple-touch-icons/114-8dae53b3bcea3f06bb139e329d1edab3.png\" sizes=\"114x114\" />\n" +
            "      <link rel=\"apple-touch-icon-precomposed\" href=\"http://cdn-qn0.jianshu.io/assets/apple-touch-icons/120-fa315ee0177dba4c55d4f66d4129b47f.png\" sizes=\"120x120\" />\n" +
            "      <link rel=\"apple-touch-icon-precomposed\" href=\"http://cdn-qn0.jianshu.io/assets/apple-touch-icons/152-052f5799bec8fb3aa624bdc72ef5bd1d.png\" sizes=\"152x152\" />\n" +
            "\n" +
            "</head>\n" +
            "\n" +
            "<body class=\"post output zh cn mac reader-day-mode reader-font2\" data-js-module=\"note-show\" data-locale=\"zh-CN\">\n" +
            "  \n" +
            "  <div class=\"navbar navbar-jianshu shrink\">\n" +
            "  <div class=\"dropdown\">\n" +
            "    <a class=\"dropdown-toggle logo\" id=\"dLabel\" role=\"button\" data-toggle=\"dropdown\" data-target=\"#\" href=\"javascript:void(0)\">\n" +
            "      简\n" +
            "    </a>\n" +
            "    <ul class=\"dropdown-menu\" role=\"menu\" aria-labelledby=\"dLabel\">\n" +
            "      <li><a href=\"/\"><i class=\"fa fa-home\"></i> 首页</a></li>\n" +
            "      <li><a href=\"/collections\"><i class=\"fa fa-th\"></i> 专题</a></li>\n" +
            "    </ul>\n" +
            "  </div>\n" +
            "</div>\n" +
            "<div class=\"navbar-user\">\n" +
            "    <a class=\"app-download-btn\" href=\"/apps/download?utm_medium=top-sugg-down&amp;utm_source=web-other-page\">App下载</a>\n" +
            "    <a class=\"login\" data-signup-link=\"true\" data-toggle=\"modal\" href=\"/sign_up\">\n" +
            "      <i class=\"fa fa-user\"></i> 注册\n" +
            "</a>    <a class=\"login\" data-signin-link=\"true\" data-toggle=\"modal\" href=\"/sign_in\">\n" +
            "      <i class=\"fa fa-sign-in\"></i> 登录\n" +
            "</a>    <a class=\"daytime set-view-mode pull-right\" href=\"javascript:void(0)\"><i class=\"fa fa-moon-o\"></i></a>\n" +
            "</div>\n" +
            "<div class=\"navbar navbar-jianshu expanded\">\n" +
            "  <div class=\"dropdown\">\n" +
            "      <a class=\"active logo\" role=\"button\" data-original-title=\"个人主页\" data-container=\"div.expanded\" href=\"/\">\n" +
            "        <b>简</b><i class=\"fa fa-home\"></i><span class=\"title\">首页</span>\n" +
            "</a>      <a data-toggle=\"tooltip\" data-placement=\"right\" data-original-title=\"专题\" data-container=\"div.expanded\" href=\"/collections\">\n" +
            "        <i class=\"fa fa-th\"></i><span class=\"title\">专题</span>\n" +
            "</a>    <a class=\"ad-selector\" href=\"/apps\">\n" +
            "      <i class=\"fa fa-mobile\"></i><span class=\"title\">下载手机应用</span>\n" +
            "</a>    <div class=\"ad-container \">\n" +
            "      <div class=\"ad-pop\">\n" +
            "        <img class=\"ad-logo\" src=\"http://cdn-qn0.jianshu.io/assets/apple-touch-icons/228-0765f118055a1d942fc286fb55f37773.png\" alt=\"228\" />\n" +
            "        <p class=\"ad-title\">简书</p>\n" +
            "        <p class=\"ad-subtitle\">交流故事，沟通想法</p>\n" +
            "        <img class=\"ad-qrcode\" src=\"http://cdn-qn0.jianshu.io/assets/app-page/download-app-qrcode-053849fa25f9b44573ef8dd3c118a20f.png\" alt=\"Download app qrcode\" />\n" +
            "        <div>\n" +
            "          <a class=\"ad-link\" href=\"https://itunes.apple.com/cn/app/jian-shu/id888237539?ls=1&amp;mt=8\">iOS</a>·\n" +
            "          <a class=\"ad-link\" href=\"http://downloads.jianshu.io/apps/haruki/JianShu-1.11.5.apk\">Android</a>\n" +
            "        </div>\n" +
            "      </div>\n" +
            "    </div>\n" +
            "  </div>\n" +
            "  <div class=\"nav-user\">\n" +
            "      <a href='#view-mode-modal' data-toggle='modal'><i class=\"fa fa-font\"></i><span class=\"title\">显示模式</span></a>\n" +
            "\n" +
            "      <a class=\"signin\" data-signin-link=\"true\" data-toggle=\"modal\" data-placement=\"right\" data-original-title=\"登录\" data-container=\"div.expanded\" href=\"/sign_in\">\n" +
            "        <i class=\"fa fa-sign-in\"></i><span class=\"title\">登录</span>\n" +
            "</a>    </div>\n" +
            "  </div>\n" +
            "\n" +
            "  \n" +
            "      <div class=\"popover-wrap\">\n" +
            "       <div class=\"fixed-btn note-fixed-download\" data-toggle=\"popover\" data-placement=\"left\" data-html=\"true\" data-trigger=\"hover\" data-content='<img src=\"http://cdn-qn0.jianshu.io/assets/app-page/note-qrcode-599086b03613e4f65dce8698fe9bcc99.png\" alt=\"Note qrcode\" />'>\n" +
            "        <a class=\"qrcode\" href=\"javascript:void(0)\">\n" +
            "          <img src=\"http://cdn-qn0.jianshu.io/assets/icon_qrcode-1f27688ec77df5a8c6c6ea70aa181874.png\" alt=\"Icon qrcode\" />\n" +
            "          <div>简书手机版下载</div>\n" +
            "</a>      </div>\n" +
            "    </div>\n" +
            "    <div class=\"fixed-btn\">\n" +
            "      <a class=\"go-top hide-go-top\" href=\"#\"> <i class=\"fa fa-angle-up\"></i></a>\n" +
            "      <a class=\"writer\" href=\"/writer#/\" data-toggle=\"tooltip\" data-placement=\"left\" title=\"写篇文章\"><i class=\"fa fa-pencil\"></i></a>\n" +
            "    </div>\n" +
            "\n" +
            "\n" +
            "  <div id=\"show-note-container\">\n" +
            "    \n" +
            "<div class=\"post-bg\" id=\"flag\">\n" +
            "  <div class=\"wrap-btn\">\n" +
            "    <!-- Notebook and collections button upper left -->\n" +
            "      <div class=\"article-related pull-left\">\n" +
            "          <a class=\"collection-menu-btn\" data-toggle=\"modal\" data-target=\"#collection-menu\" href=\"javascript:void(null);\">\n" +
            "            <i class=\"fa fa-th\"></i>\n" +
            "</a>          <a class=\"notebooks-menu-btn\" data-toggle=\"modal\" data-target=\"#notebooks-menu\" href=\"javascript:void(null);\"><i class=\"fa fa-list-ul\"></i></a>\n" +
            "        <div class=\"related-avatar-group activities\"></div>\n" +
            "      </div>\n" +
            "    <!-- ******* -->\n" +
            "\n" +
            "      <a class=\"app-download-btn\" href=\"/apps/download?utm_medium=top-sugg-down&amp;utm_source=note-show\">App下载</a>\n" +
            "  <a class=\"login\" data-signup-link=\"true\" data-toggle=\"modal\" href=\"/sign_up\">\n" +
            "    <i class=\"fa fa-user\"></i> 注册\n" +
            "</a>  <a class=\"login\" data-signin-link=\"true\" data-toggle=\"modal\" href=\"/sign_in\">\n" +
            "    <i class=\"fa fa-sign-in\"></i> 登录\n" +
            "</a>\n" +
            "\n" +
            "    <!-- Buttons upper right -->\n" +
            "    <!-- ******* -->\n" +
            "  </div>\n" +
            "\n" +
            "  <div class=\"container\">\n" +
            "    <!-- Article activities for width under 768px -->\n" +
            "    <div class=\"related-avatar-group activities\"></div>\n" +
            "      <div class=\"article\">\n" +
            "        <div class=\"preview\">\n" +
            "          <div class=\"author-info\">\n" +
            "              <div class=\"btn btn-small btn-success follow\">\n" +
            "    <a data-signin-link=\"true\" data-toggle=\"modal\" href=\"/sign_in\"><i class=\"fa fa-plus\"></i>  <span>添加关注</span></a>\n" +
            "  </div>\n" +
            "\n" +
            "            <a class=\"avatar\" href=\"/users/173dd2126629\">\n" +
            "              <img src=\"http://upload.jianshu.io/users/upload_avatars/580359/dff73e1a5c40.jpg?imageMogr/thumbnail/90x90/quality/100\" alt=\"100\" />\n" +
            "</a>            <span class=\"label\">\n" +
            "                作者\n" +
            "            </span>\n" +
            "            <a class=\"author-name blue-link\" href=\"/users/173dd2126629\">\n" +
            "              <span>TestDevTalk</span>\n" +
            "</a>              <span data-toggle='tooltip' data-original-title='最后编辑于 2015.06.04 16:19'>2015.06.04 14:33*</span>\n" +
            "            <div>\n" +
            "              <span>写了10400字</span>，<span>被436人关注</span>，<span>获得了289个喜欢</span>\n" +
            "            </div>\n" +
            "          </div>\n" +
            "          <h1 class=\"title\">在Android Studio中进行单元测试和UI测试</h1>\n" +
            "          <div class=\"meta-top\">\n" +
            "            <span class=\"wordage\"></span>\n" +
            "            <span class=\"views-count\"></span>\n" +
            "            <span class=\"comments-count\"></span>\n" +
            "            <span class=\"likes-count\"></span>\n" +
            "          </div>\n" +
            "\n" +
            "          <!-- Collection/Bookmark/Share for width under 768px -->\n" +
            "          <div class=\"article-share\"></div>\n" +
            "          <!-- -->\n" +
            "\n" +
            "          <div class=\"show-content\"><blockquote><p>本篇教程翻译自<a href=\"https://events.google.com/io2015/\" target=\"_blank\">Google I/O 2015</a>中关于测试的codelab，掌握科学上网的同学请点击这里阅读：<a href=\"https://io2015codelabs.appspot.com/codelabs/android-studio-testing#1\" target=\"_blank\">Unit and UI Testing in Android Studio</a>。能力有限，如有翻译错误，请批评指正。如需转载，请注明出处。<br><a href=\"https://github.com/dongdaqing/TestingExample\" target=\"_blank\">Github下载测试源码</a></p></blockquote>\n" +
            "<h2><strong>目录</strong></h2>\n" +
            "<blockquote><ul>\n" +
            "<li><a href=\"http://www.jianshu.com/p/de17655125f5\" target=\"_blank\">在Android Studio中进行单元测试和UI测试 - 1.概述</a></li>\n" +
            "<li><a href=\"http://www.jianshu.com/p/e02176b5321b\" target=\"_blank\">在Android Studio中进行单元测试和UI测试 - 2.创建新的Android Studio工程</a></li>\n" +
            "<li><a href=\"http://www.jianshu.com/p/860ac28b7d31\" target=\"_blank\">在Android Studio中进行单元测试和UI测试 - 3.配置支持单元测试的工程</a></li>\n" +
            "<li><a href=\"http://www.jianshu.com/p/3685a601b388\" target=\"_blank\">在Android Studio中进行单元测试和UI测试 - 4.创建第一个单元测试</a></li>\n" +
            "<li><a href=\"http://www.jianshu.com/p/18833d950bc9\" target=\"_blank\">在Android Studio中进行单元测试和UI测试 - 5.运行单元测试</a></li>\n" +
            "<li><a href=\"http://www.jianshu.com/p/dbc81b1fec05\" target=\"_blank\">在Android Studio中进行单元测试和UI测试 - 6.配置支持Instrumentation测试的工程</a></li>\n" +
            "<li><a href=\"http://www.jianshu.com/p/6002d7965f07\" target=\"_blank\">在Android Studio中进行单元测试和UI测试 - 7.为app添加简单的交互</a></li>\n" +
            "<li><a href=\"http://www.jianshu.com/p/e6acb7b34e14\" target=\"_blank\">在Android Studio中进行单元测试和UI测试 - 8.创建并运行Espresso测试</a></li>\n" +
            "<li><a href=\"http://www.jianshu.com/p/7476c0f0c400\" target=\"_blank\">在Android Studio中进行单元测试和UI测试 - 9.祝贺！</a></li>\n" +
            "</ul></blockquote>\n" +
            "<hr>\n" +
            "<h2>1.概述</h2>\n" +
            "<p>在这个codelab中，你将学习如何在Android Studio中配置工程用于测试，在开发机器上编写并运行单元测试，以及如何在手机上做功能UI测试。</p>\n" +
            "<p><strong>你会学到什么</strong></p>\n" +
            "<ul>\n" +
            "<li>更新包含JUnit和Android Testing Support Library的Gradle构建文件</li>\n" +
            "<li>编写运行在本机Java虚拟机上的单元测试</li>\n" +
            "<li>编写运行在手机或者虚拟机上的Espresso测试</li>\n" +
            "</ul>\n" +
            "<p><strong>你需要什么</strong></p>\n" +
            "<ul>\n" +
            "<li>\n" +
            "<a href=\"https://developer.android.com/sdk/installing/studio.html\" target=\"_blank\">Android Studio</a> v1.2+</li>\n" +
            "<li>Android 4.0+的测试设备</li>\n" +
            "</ul>\n" +
            "<hr>\n" +
            "<h2>2.创建新的Android Studio工程</h2>\n" +
            "<p>如果是第一次启动Android Studio，从欢迎页选择“<strong>Start a new Android Studio project</strong>”。如果已经打开了一个工程，选择<strong>File&gt;New&gt;New Project...</strong></p>\n" +
            "<p>“<em>Create new project</em>”向导会指导整个过程，在第一页输入如下内容：</p>\n" +
            "<table>\n" +
            "<thead>\n" +
            "<tr>\n" +
            "<th>Setting</th>\n" +
            "<th>Value</th>\n" +
            "</tr>\n" +
            "</thead>\n" +
            "<tbody>\n" +
            "<tr>\n" +
            "<td><strong>Application Name</strong></td>\n" +
            "<td>TestingExample</td>\n" +
            "</tr>\n" +
            "<tr>\n" +
            "<td><strong>Company demain</strong></td>\n" +
            "<td>testing.example.com</td>\n" +
            "</tr>\n" +
            "</tbody>\n" +
            "</table>\n" +
            "<p>这样会保证你的代码同codelab讲解的内容具有一致的命名。其他的选项都设置为默认，一路点击<strong>Next</strong>直到工程创建完毕。</p>\n" +
            "<p>点击<strong>Run</strong>按钮检查app是否运行正常，要么从模拟器列表中选择一个启动，要么确认开启了debug模式的设备通过USB同电脑正确连接。</p>\n" +
            "<p>app目前没有做任何事情，但是屏幕上应该显示“Hello world!”和app的名字。</p>\n" +
            "<div class=\"image-package\">\n" +
            "<img src=\"http://upload-images.jianshu.io/upload_images/580359-839b775d39f912f6.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240\" alt=\"\" data-original-src=\"http://upload-images.jianshu.io/upload_images/580359-839b775d39f912f6.png\"><br><div class=\"image-caption\"></div>\n" +
            "</div>\n" +
            "<p><strong>经常被问及的问题</strong></p>\n" +
            "<ul>\n" +
            "<li><a href=\"https://developer.android.com/sdk/index.html\" target=\"_blank\">如何安装Android Studio？</a></li>\n" +
            "<li><a href=\"http://developer.android.com/tools/device.html\" target=\"_blank\">如何开启USB调试？</a></li>\n" +
            "<li><a href=\"http://stackoverflow.com/questions/16596877/android-studio-doesnt-see-device\" target=\"_blank\">为什么Android Studio找不到我的设备？</a></li>\n" +
            "<li><a href=\"http://stackoverflow.com/questions/4775603/android-error-failed-to-install-apk-on-device-timeout/4786299#4786299\" target=\"_blank\">Android错误：无法将*.apk安装到设备上：超时？</a></li>\n" +
            "</ul>\n" +
            "<hr>\n" +
            "<h2>3.配置支持单元测试的工程</h2>\n" +
            "<p>在写测试之前，让我们做下简单的检查，确保工程配置正确。</p>\n" +
            "<p>首先，确认在<strong>Build Variants</strong>窗口内的<strong>Test Artifact</strong>中选择了\"Unit Tests\"。</p>\n" +
            "<div class=\"image-package\">\n" +
            "<img src=\"http://upload-images.jianshu.io/upload_images/580359-ab4402443ad7dc5f.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240\" alt=\"\" data-original-src=\"http://upload-images.jianshu.io/upload_images/580359-ab4402443ad7dc5f.png\"><br><div class=\"image-caption\"></div>\n" +
            "</div>\n" +
            "<p>然后，在工程的<code>src</code>文件夹内创建<code>test</code>和<code>test/java</code>文件夹。需要注意的是，你不能在<strong>Android</strong>视图下进行这些操作，要么在系统的文件管理器内创建，要么在工程窗口左上方点击下拉菜单选择<strong>Project</strong>视图。最终的工程结构应该是这样的：</p>\n" +
            "<div class=\"image-package\">\n" +
            "<img src=\"http://upload-images.jianshu.io/upload_images/580359-9e098817f6fcca44.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240\" alt=\"\" data-original-src=\"http://upload-images.jianshu.io/upload_images/580359-9e098817f6fcca44.png\"><br><div class=\"image-caption\"></div>\n" +
            "</div>\n" +
            "<p>（在codelab的剩余部分，你可以返回继续使用<strong>Android</strong>工程视图）</p>\n" +
            "<p>最后，打开工程的<code>build.gradle（Module:app）</code>文件，添加JUnit4依赖，点击<strong>Gradle sync</strong>按钮。</p>\n" +
            "<p><strong>build.gradle</strong></p>\n" +
            "<pre><code>dependencies {\n" +
            "    compile fileTree(dir: 'libs', include: ['*.jar'])\n" +
            "    compile 'com.android.support:appcompat-v7:22.1.1'\n" +
            "    testCompile 'junit:junit:4.12'\n" +
            "}</code></pre>\n" +
            "<blockquote><p>当你同步Gradle配置时，可能需要联网下载JUnit依赖。</p></blockquote>\n" +
            "<hr>\n" +
            "<h2>4.创建第一个单元测试</h2>\n" +
            "<p>现在，万事俱备，让我们开始写第一个测试吧。首先，创建一个非常简单的被测类：Calculator类。</p>\n" +
            "<div class=\"image-package\">\n" +
            "<img src=\"http://upload-images.jianshu.io/upload_images/580359-20cce1345b5076de.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240\" alt=\"\" data-original-src=\"http://upload-images.jianshu.io/upload_images/580359-20cce1345b5076de.png\"><br><div class=\"image-caption\"></div>\n" +
            "</div>\n" +
            "<p>然后，向类中添加一些基本的算术运算方法，比如加法和减法。将下列代码复制到编辑器中。不用担心实际的实现，暂时让所有的方法返回0。</p>\n" +
            "<p><strong>Calculator.java</strong></p>\n" +
            "<pre><code>package com.example.testing.testingexample;\n" +
            "\n" +
            "public class Calculator {\n" +
            "\n" +
            "    public double sum(double a, double b){\n" +
            "        return 0;\n" +
            "    }\n" +
            "\n" +
            "    public double substract(double a, double b){\n" +
            "        return 0;\n" +
            "    }\n" +
            "\n" +
            "    public double divide(double a, double b){\n" +
            "        return 0;\n" +
            "    }\n" +
            "\n" +
            "    public double multiply(double a, double b){\n" +
            "        return 0;\n" +
            "    }\n" +
            "}</code></pre>\n" +
            "<p>Android Studio提供了一个快速创建测试类的方法。只需在编辑器内右键点击Calculator类的声明，选择<strong>Go to &gt; Test</strong>，然后<strong>\"Create a new test…\"</strong></p>\n" +
            "<div class=\"image-package\">\n" +
            "<img src=\"http://upload-images.jianshu.io/upload_images/580359-729c021ff61b0dc7.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240\" alt=\"\" data-original-src=\"http://upload-images.jianshu.io/upload_images/580359-729c021ff61b0dc7.png\"><br><div class=\"image-caption\"></div>\n" +
            "</div>\n" +
            "<p>在打开的对话窗口中，选择<strong>JUnit4</strong>和\"<strong>setUp/@Before</strong>\"，同时为所有的计算器运算生成测试方法。</p>\n" +
            "<div class=\"image-package\">\n" +
            "<img src=\"http://upload-images.jianshu.io/upload_images/580359-19f96a03d2fa811a.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240\" alt=\"\" data-original-src=\"http://upload-images.jianshu.io/upload_images/580359-19f96a03d2fa811a.png\"><br><div class=\"image-caption\"></div>\n" +
            "</div>\n" +
            "<p>这样，就会在正确的文件夹内<code>(app/src/test/java/com/example/testing/testingexample)</code>生成测试类框架，在框架内填入测试方法即可。下面是一个示例：</p>\n" +
            "<p><strong>Calculator.java</strong></p>\n" +
            "<pre><code>package com.example.testing.testingexample;\n" +
            "\n" +
            "import org.junit.Before;\n" +
            "import org.junit.Test;\n" +
            "\n" +
            "import static org.junit.Assert.*;\n" +
            "\n" +
            "public class CalculatorTest {\n" +
            "\n" +
            "    private Calculator mCalculator;\n" +
            "\n" +
            "    @Before\n" +
            "    public void setUp() throws Exception {\n" +
            "        mCalculator = new Calculator();\n" +
            "    }\n" +
            "\n" +
            "    @Test\n" +
            "    public void testSum() throws Exception {\n" +
            "        //expected: 6, sum of 1 and 5\n" +
            "        assertEquals(6d, mCalculator.sum(1d, 5d), 0);\n" +
            "    }\n" +
            "\n" +
            "    @Test\n" +
            "    public void testSubstract() throws Exception {\n" +
            "        assertEquals(1d, mCalculator.substract(5d, 4d), 0);\n" +
            "    }\n" +
            "\n" +
            "    @Test\n" +
            "    public void testDivide() throws Exception {\n" +
            "        assertEquals(4d, mCalculator.divide(20d, 5d), 0);\n" +
            "    }\n" +
            "\n" +
            "    @Test\n" +
            "    public void testMultiply() throws Exception {\n" +
            "        assertEquals(10d, mCalculator.multiply(2d, 5d), 0);\n" +
            "    }\n" +
            "}</code></pre>\n" +
            "<p>请将代码复制到编辑器或者使用JUnit框架提供的断言来编写自己的测试。</p>\n" +
            "<hr>\n" +
            "<h2>5.运行单元测试</h2>\n" +
            "<p>终于到运行测试的时候了！右键点击<code>CalculatorTest</code>类，选择<strong>Run &gt; CalculatorTest</strong>。也可以通过命令行运行测试，在工程目录内输入：</p>\n" +
            "<pre><code>./gradlew test</code></pre>\n" +
            "<p>无论如何运行测试，都应该看到输出显示4个测试都失败了。这是预期的结果，因为我们还没有实现运算操作。</p>\n" +
            "<div class=\"image-package\">\n" +
            "<img src=\"http://upload-images.jianshu.io/upload_images/580359-00a07e968baebccc.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240\" alt=\"\" data-original-src=\"http://upload-images.jianshu.io/upload_images/580359-00a07e968baebccc.png\"><br><div class=\"image-caption\"></div>\n" +
            "</div>\n" +
            "<p>让我们修改Calculator类中的<code>sum(double a, double b)</code>方法返回一个正确的结果，重新运行测试。你应该看到4个测试中的3个失败了。</p>\n" +
            "<p><strong>Calculator.java</strong></p>\n" +
            "<pre><code>public double sum(double a, double b){\n" +
            "    return a + b;\n" +
            "}</code></pre>\n" +
            "<p>作为练习，你可以实现剩余的方法使所有的测试通过。</p>\n" +
            "<blockquote><p>可能你已经注意到了Android Studio从来没有让你连接设备或者启动模拟器来运行测试。那是因为，位于<code>src/tests</code>目录下的测试是运行在本地电脑Java虚拟机上的单元测试。编写测试，实现功能使测试通过，然后再添加更多的测试...这种工作方式使快速迭代成为可能，我们称之为<strong>测试驱动开发</strong>。<br>值得注意的是，当在本地运行测试时，Gradle为你在环境变量中提供了包含Android框架的android.jar包。但是它们功能不完整（所以，打个比方，你不能单纯调用<code>Activity</code>的方法并指望它们生效）。推荐使用<a href=\"http://mockito.org/\" target=\"_blank\">Mockito</a>等mocking框架来mock你需要使用的任何Android方法。对于运行在设备上，并充分利用Android框架的测试，请继续阅读本篇教程的下个部分。</p></blockquote>\n" +
            "<hr>\n" +
            "<h2>6.配置支持Instrumentation测试的工程</h2>\n" +
            "<p>虽然在Android框架内支持运行instrumentation测试，但是目前开发重心主要集中在刚刚发布的作为<strong>Android Testing Support Library</strong>一部分的新的<code>AndroidJUnitRunner</code>。测试库包含<em>Espresso</em>，用于运行功能UI测试的框架。让我们通过编辑<code>build.gradle</code>的相关部分来把它们添加进我们的工程。</p>\n" +
            "<p><strong>build.gradle</strong></p>\n" +
            "<pre><code>apply plugin: 'com.android.application'\n" +
            "\n" +
            "android {\n" +
            "    compileSdkVersion 22\n" +
            "    buildToolsVersion \"22.0.1\"\n" +
            "\n" +
            "    defaultConfig {\n" +
            "        applicationId \"com.example.testing.testingexample\"\n" +
            "        minSdkVersion 15\n" +
            "        targetSdkVersion 22\n" +
            "        versionCode 1\n" +
            "        versionName \"1.0\"\n" +
            "\n" +
            "        //ADD THIS LINE:\n" +
            "        testInstrumentationRunner \"android.support.test.runner.AndroidJUnitRunner\"\n" +
            "    }\n" +
            "\n" +
            "    buildTypes {\n" +
            "        release {\n" +
            "            minifyEnabled false\n" +
            "            proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'\n" +
            "        }\n" +
            "    }\n" +
            "\n" +
            "    //ADD THESE LINES:\n" +
            "    packagingOptions {\n" +
            "        exclude 'LICENSE.txt'\n" +
            "    }\n" +
            "}\n" +
            "\n" +
            "dependencies {\n" +
            "    compile fileTree(dir: 'libs', include: ['*.jar'])\n" +
            "    compile 'com.android.support:appcompat-v7:22.0.0' //← MAKE SURE IT’S 22.0.0\n" +
            "    testCompile 'junit:junit:4.12'\n" +
            "\n" +
            "    //ADD THESE LINES:\n" +
            "    androidTestCompile 'com.android.support.test:runner:0.2'\n" +
            "    androidTestCompile 'com.android.support.test:rules:0.2'\n" +
            "    androidTestCompile 'com.android.support.test.espresso:espresso-core:2.1'\n" +
            "}</code></pre>\n" +
            "<blockquote><p><strong>重要</strong>：由于一些依赖版本冲突，你需要确认<code>com.android.support:appcompat-v7</code>库的版本号是<code>22.0.0</code>，像上面的代码片段一样。<br>另外，Android Studio可能会提醒你<code>Build Tools 22.0.1</code>没有安装。你应该接受修复建议，Studio会为你安装Build Tools或者在build.gradle中把这行修改成已经安装在你电脑的版本。</p></blockquote>\n" +
            "<p>上面的工作完成后，在<strong>Build Variants</strong>窗口内切换成<strong>Android Instrumentation Tests</strong>，你的工程应该自动同步。如果没有，点击<strong>Gradle sync</strong>按钮。</p>\n" +
            "<hr>\n" +
            "<h2>7.为app添加简单的交互</h2>\n" +
            "<div class=\"image-package\">\n" +
            "<img src=\"http://upload-images.jianshu.io/upload_images/580359-40a6436d81203a3d.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240\" alt=\"\" data-original-src=\"http://upload-images.jianshu.io/upload_images/580359-40a6436d81203a3d.png\"><br><div class=\"image-caption\"></div>\n" +
            "</div>\n" +
            "<p>在使用Espresso进行UI测试前，让我们为app添加一些Views和简单的交互。我们使用一个用户可以输入名字的EditText，欢迎用户的Button和用于输出的TextView。打开<code>res/layout/activity_main.xml</code>，粘贴如下代码：<br><strong>activity_main.xml</strong></p>\n" +
            "<pre><code>&lt;RelativeLayout xmlns:android=\"http://schemas.android.com/apk/res/android\"\n" +
            "    xmlns:tools=\"http://schemas.android.com/tools\" android:layout_width=\"match_parent\"\n" +
            "    android:layout_height=\"match_parent\" android:paddingLeft=\"@dimen/activity_horizontal_margin\"\n" +
            "    android:paddingRight=\"@dimen/activity_horizontal_margin\"\n" +
            "    android:paddingTop=\"@dimen/activity_vertical_margin\"\n" +
            "    android:paddingBottom=\"@dimen/activity_vertical_margin\" tools:context=\".MainActivity\"&gt;\n" +
            "\n" +
            "    &lt;TextView\n" +
            "        android:id=\"@+id/textView\"\n" +
            "        android:text=\"@string/hello_world\" android:layout_width=\"wrap_content\"\n" +
            "        android:layout_height=\"wrap_content\" /&gt;\n" +
            "    &lt;EditText\n" +
            "        android:hint=\"Enter your name here\"\n" +
            "        android:id=\"@+id/editText\"\n" +
            "        android:layout_width=\"match_parent\"\n" +
            "        android:layout_height=\"wrap_content\"\n" +
            "        android:layout_below=\"@+id/textView\"/&gt;\n" +
            "    &lt;Button\n" +
            "        android:layout_width=\"match_parent\"\n" +
            "        android:layout_height=\"wrap_content\"\n" +
            "        android:text=\"Say hello!\"\n" +
            "        android:layout_below=\"@+id/editText\"\n" +
            "        android:onClick=\"sayHello\"/&gt;\n" +
            "&lt;/RelativeLayout&gt;</code></pre>\n" +
            "<p>还需要在<code>MainActivity.java</code>中添加onClick handler：</p>\n" +
            "<p><strong>MainActivity.java</strong></p>\n" +
            "<pre><code>public void sayHello(View v){\n" +
            "    TextView textView = (TextView) findViewById(R.id.textView);\n" +
            "    EditText editText = (EditText) findViewById(R.id.editText);\n" +
            "    textView.setText(\"Hello, \" + editText.getText().toString() + \"!\");\n" +
            "}</code></pre>\n" +
            "<p>现在可以运行app并确认一切工作正常。在点击<strong>Run</strong>按钮之前，确认你的<em>Run Configuration</em>没有设置为运行测试。如需更改，点击下拉选项，选择<strong>app</strong>。</p>\n" +
            "<hr>\n" +
            "<h2>8.创建并运行Espresso测试</h2>\n" +
            "<div class=\"image-package\">\n" +
            "<img src=\"http://upload-images.jianshu.io/upload_images/580359-182d42c3cc27596a.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240\" alt=\"\" data-original-src=\"http://upload-images.jianshu.io/upload_images/580359-182d42c3cc27596a.png\"><br><div class=\"image-caption\"></div>\n" +
            "</div>\n" +
            "<p>在工程的整体视图上，找到以（<code>androidTest</code>）后缀结尾的包名并创建一个新的Java类。可以将它命名为<code>MainActivityInstrumentationTest</code>，将如下代码粘贴过去。</p>\n" +
            "<p><em>** MainActivityInstrumentationTest.java</em></p>\n" +
            "<pre><code>package com.example.testing.testingexample;\n" +
            "\n" +
            "import android.support.test.InstrumentationRegistry;\n" +
            "import android.support.test.espresso.action.ViewActions;\n" +
            "import android.support.test.rule.ActivityTestRule;\n" +
            "import android.support.test.runner.AndroidJUnit4;\n" +
            "import android.test.ActivityInstrumentationTestCase2;\n" +
            "import android.test.suitebuilder.annotation.LargeTest;\n" +
            "\n" +
            "import org.junit.After;\n" +
            "import org.junit.Before;\n" +
            "import org.junit.Rule;\n" +
            "import org.junit.Test;\n" +
            "import org.junit.runner.RunWith;\n" +
            "\n" +
            "import static android.support.test.espresso.Espresso.onView;\n" +
            "import static android.support.test.espresso.action.ViewActions.click;\n" +
            "import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;\n" +
            "import static android.support.test.espresso.action.ViewActions.typeText;\n" +
            "import static android.support.test.espresso.assertion.ViewAssertions.matches;\n" +
            "import static android.support.test.espresso.matcher.ViewMatchers.withId;\n" +
            "import static android.support.test.espresso.matcher.ViewMatchers.withText;\n" +
            "\n" +
            "@RunWith(AndroidJUnit4.class)\n" +
            "@LargeTest\n" +
            "public class MainActivityInstrumentationTest {\n" +
            "\n" +
            "    private static final String STRING_TO_BE_TYPED = \"Peter\";\n" +
            "\n" +
            "    @Rule\n" +
            "    public ActivityTestRule&lt;MainActivity&gt; mActivityRule = new ActivityTestRule&lt;&gt;(\n" +
            "        MainActivity.class);\n" +
            "\n" +
            "    @Test\n" +
            "    public void sayHello(){\n" +
            "        onView(withId(R.id.editText)).perform(typeText(STRING_TO_BE_TYPED), closeSoftKeyboard()); //line 1\n" +
            "\n" +
            "        onView(withText(\"Say hello!\")).perform(click()); //line 2\n" +
            "\n" +
            "        String expectedText = \"Hello, \" + STRING_TO_BE_TYPED + \"!\";\n" +
            "        onView(withId(R.id.textView)).check(matches(withText(expectedText))); //line 3\n" +
            "    }\n" +
            "\n" +
            "}</code></pre>\n" +
            "<p>测试类通过<strong>AndroidJUnitRunner</strong>运行，并执行<code>sayHello()</code>方法。下面将逐行解释都做了什么：</p>\n" +
            "<ul>\n" +
            "<li>1.首先，找到ID为<code>editText</code>的view，输入<code>Peter</code>，然后关闭键盘；</li>\n" +
            "<li>2.接下来，点击<code>Say hello!</code>的View，我们没有在布局的XML中为这个Button设置id，因此，通过搜索它上面的文字来找到它；</li>\n" +
            "<li>3.最后，将<code>TextView</code>上的文本同预期结果对比，如果一致则测试通过；</li>\n" +
            "</ul>\n" +
            "<p>你也可以右键点击域名运行测试，选择<strong>Run&gt;MainActivityInstrume...</strong>（第二个带Android图标的）</p>\n" +
            "<div class=\"image-package\">\n" +
            "<img src=\"http://upload-images.jianshu.io/upload_images/580359-86da68654bd41cb1.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240\" alt=\"\" data-original-src=\"http://upload-images.jianshu.io/upload_images/580359-86da68654bd41cb1.png\"><br><div class=\"image-caption\"></div>\n" +
            "</div>\n" +
            "<p>这样就会在模拟器或者连接的设备上运行测试，你可以在手机屏幕上看到被执行的动作（比如在<code>EditText</code>上打字）。最后会在Android Studio输出通过和失败的测试结果。</p>\n" +
            "<p><a href=\"https://github.com/dongdaqing/TestingExample\" target=\"_blank\">Github下载测试源码</a></p>\n" +
            "<hr>\n" +
            "<h2>9.祝贺</h2>\n" +
            "<p>我们希望你能喜欢本篇教程，并且开始着手测试你的应用程序。接着你可以学习如下内容：</p>\n" +
            "<ul>\n" +
            "<li>了解更多关于<a href=\"https://developer.android.com/training/testing/unit-testing/index.html\" target=\"_blank\">单元测试和instrumentation测试的区别</a>；</li>\n" +
            "<li>了解更多关于设置<a href=\"https://developer.android.com/tools/testing-support-library/index.html\" target=\"_blank\">Android Testing Support Library</a>；</li>\n" +
            "<li>\n" +
            "<p>观看下面非常棒的有关Android Studio的视频：</p>\n" +
            "<ul>\n" +
            "<li><a href=\"https://www.youtube.com/watch?v=K2dodTXARqc\" target=\"_blank\">Introduction to Android Studio</a></li>\n" +
            "<li><a href=\"https://www.youtube.com/watch?v=cD7NPxuuXYY\" target=\"_blank\">Introducing Gradle (Ep 2, Android Studio)</a></li>\n" +
            "<li><a href=\"http://www.youtube.com/watch?v=JLLnhwtDoHw\" target=\"_blank\">Layout Editor (Ep 3, Android Studio)</a></li>\n" +
            "<li><a href=\"http://www.youtube.com/watch?v=2I6fuD20qlY\" target=\"_blank\">Debugging and testing in Android Studio (Ep 4, Android Studio)</a></li>\n" +
            "</ul>\n" +
            "</li>\n" +
            "<li>在<a href=\"https://github.com/googlesamples/android-testing/\" target=\"_blank\">Github下载Google测试示例代码</a>\n" +
            "</li>\n" +
            "</ul>\n" +
            "<p>（完）</p>\n" +
            "</div>\n" +
            "        </div>\n" +
            "      </div>\n" +
            "\n" +
            "      <div class=\"visitor_edit\">\n" +
            "        <!-- further readings -->\n" +
            "        <div id=\"further-readings\" data-user-slug=\"\" data-user-nickname=\"\">\n" +
            "          <div id=\"further-reading-line\" class=\"hide further-reading-line\"></div>\n" +
            "          <ul id=\"further-readings-list\" class=\"reading-list unstyled\"></ul>\n" +
            "          <div id=\"further-reading-new\" class=\"reading-edit\">\n" +
            "              <a data-signin-link=\"true\" data-toggle=\"modal\" href=\"/sign_in\">\n" +
            "                <i class=\"fa fa-plus-circle\"></i> 推荐拓展阅读\n" +
            "</a>            <div id=\"further-reading-form\"></div>\n" +
            "          </div>\n" +
            "        </div>\n" +
            "        <div class=\"pull-right\">\n" +
            "          <!-- copyright -->\n" +
            "          <div class=\"author-copyright pull-right\" data-toggle=\"tooltip\" data-html=\"true\" title=\"转载请联系作者获得授权，并标注“简书作者”。\">\n" +
            "            <a><i class=\"fa fa-copyright\"></i> 著作权归作者所有</a>\n" +
            "          </div>\n" +
            "        </div>\n" +
            "      </div>\n" +
            "\n" +
            "      <!-- Reward / Support Author -->\n" +
            "        <div class=\"support-author\">\n" +
            "          <p>如果觉得我的文章对您有用，请随意打赏。您的支持将鼓励我继续创作！</p>\n" +
            "            <a class=\"btn btn-pay\" data-toggle=\"modal\" href=\"#pay-modal\">¥ 打赏支持</a>\n" +
            "          <div class=\"avatar-list\"></div>\n" +
            "        </div>\n" +
            "\n" +
            "      <!-- article meta bottom -->\n" +
            "      <div class=\"meta-bottom clearfix\">\n" +
            "        <!--  Like Button -->\n" +
            "        <div class=\"like \">\n" +
            "            <div class=\"like-button\">\n" +
            "              <a id=\"like-note\" class=\"like-content\" data-signin-link=\"true\" data-toggle=\"modal\" href=\"/sign_in\">\n" +
            "                <i class=\"fa fa-heart-o\"></i>  喜欢\n" +
            "</a></div>          <span id=\"likes-count\" data-toggle=\"tooltip\" data-placement=\"right\" title=\"查看所有喜欢的用户\">\n" +
            "            <i class=\"fa fa-spinner fa-pulse\"></i>\n" +
            "</span>        </div>\n" +
            "        <!--  share group -->\n" +
            "        <div class=\"share-group pull-right\">\n" +
            "          <a href=\"javascript:void((function(s,d,e,r,l,p,t,z,c){var%20f=&#39;http://v.t.sina.com.cn/share/share.php?appkey=1881139527&#39;,u=z||d.location,p=[&#39;&amp;url=&#39;,e(u),&#39;&amp;title=&#39;,e(t||d.title),&#39;&amp;source=&#39;,e(r),&#39;&amp;sourceUrl=&#39;,e(l),&#39;&amp;content=&#39;,c||&#39;gb2312&#39;,&#39;&amp;pic=&#39;,e(p||&#39;&#39;)].join(&#39;&#39;);function%20a(){if(!window.open([f,p].join(&#39;&#39;),&#39;mb&#39;,[&#39;toolbar=0,status=0,resizable=1,width=440,height=430,left=&#39;,(s.width-440)/2,&#39;,top=&#39;,(s.height-430)/2].join(&#39;&#39;)))u.href=[f,p].join(&#39;&#39;);};if(/Firefox/.test(navigator.userAgent))setTimeout(a,0);else%20a();})(screen,document,encodeURIComponent,&#39;&#39;,&#39;&#39;,&#39;http://cwb.assets.jianshu.io/notes/images/1539608/weibo/image_0dab2954331f.jpg&#39;, &#39;推荐 TestDevTalk 的文章《在Android Studio中进行单元测试和UI测试》（ 分享自 @简书 ）&#39;,&#39;http://www.jianshu.com/p/03118c11c199&#39;,&#39;页面编码gb2312|utf-8默认gb2312&#39;));\" data-name=\"weibo\">\n" +
            "            <i class=\"fa fa-weibo\"></i><span>分享到微博</span>\n" +
            "          </a>\n" +
            "          <a data-toggle=\"modal\" href=\"#share-weixin-modal\" data-name=\"weixin\">\n" +
            "            <i class=\"fa fa-weixin\"></i><span>分享到微信</span>\n" +
            "          </a>\n" +
            "          <div class=\"more\">\n" +
            "            <a href=\"javascript:void(0)\" data-toggle=\"dropdown\">更多分享<i class=\"fa fa-caret-down\"></i></a>\n" +
            "            <ul class=\"dropdown-menu arrow-top\">\n" +
            "                <li><a href=\"http://cwb.assets.jianshu.io/notes/images/1539608/weibo/image_0dab2954331f.jpg\" target=\"_blank\" data-name=\"changweibo\"><i class=\"fa fa-arrow-circle-o-down\"></i> 下载长微博图片</a></li>\n" +
            "                <li>\n" +
            "                  <a data-name=\"tweibo\" href=\"javascript:void(function(){var d=document,e=encodeURIComponent,r=&#39;http://share.v.t.qq.com/index.php?c=share&amp;a=index&amp;url=&#39;+e(&#39;http://www.jianshu.com/p/03118c11c199&#39;)+&#39;&amp;title=&#39;+e(&#39;推荐 TestDevTalk 的文章《在Android Studio中进行单元测试和UI测试》（ 分享自 @jianshuio ）&#39;),x=function(){if(!window.open(r,&#39;tweibo&#39;,&#39;toolbar=0,resizable=1,scrollbars=yes,status=1,width=600,height=600&#39;))location.href=r};if(/Firefox/.test(navigator.userAgent)){setTimeout(x,0)}else{x()}})();\">\n" +
            "                    <img src=\"http://baijii-common.b0.upaiyun.com/social_icons/32x32/tweibo.png\" alt=\"Tweibo\" /> 分享到腾讯微博\n" +
            "</a>                </li>\n" +
            "                <li>\n" +
            "                  <a data-name=\"qzone\" href=\"javascript:void(function(){var d=document,e=encodeURIComponent,r=&#39;http://sns.qzone.qq.com/cgi-bin/qzshare/cgi_qzshare_onekey?url=&#39;+e(&#39;http://www.jianshu.com/p/03118c11c199&#39;)+&#39;&amp;title=&#39;+e(&#39;推荐 TestDevTalk 的文章《在Android Studio中进行单元测试和UI测试》&#39;),x=function(){if(!window.open(r,&#39;qzone&#39;,&#39;toolbar=0,resizable=1,scrollbars=yes,status=1,width=600,height=600&#39;))location.href=r};if(/Firefox/.test(navigator.userAgent)){setTimeout(x,0)}else{x()}})();\">\n" +
            "                    <img src=\"http://baijii-common.b0.upaiyun.com/social_icons/32x32/qzone.png\" alt=\"Qzone\" /> 分享到QQ空间\n" +
            "</a>                </li>\n" +
            "                <li>\n" +
            "                  <a data-name=\"twitter\" href=\"javascript:void(function(){var d=document,e=encodeURIComponent,r=&#39;https://twitter.com/share?url=&#39;+e(&#39;http://www.jianshu.com/p/03118c11c199&#39;)+&#39;&amp;text=&#39;+e(&#39;推荐 TestDevTalk 的文章《在Android Studio中进行单元测试和UI测试》（ 分享自 @jianshucom ）&#39;)+&#39;&amp;related=&#39;+e(&#39;jianshucom&#39;),x=function(){if(!window.open(r,&#39;twitter&#39;,&#39;toolbar=0,resizable=1,scrollbars=yes,status=1,width=600,height=600&#39;))location.href=r};if(/Firefox/.test(navigator.userAgent)){setTimeout(x,0)}else{x()}})();\">\n" +
            "                    <img src=\"http://baijii-common.b0.upaiyun.com/social_icons/32x32/twitter.png\" alt=\"Twitter\" /> 分享到Twitter\n" +
            "</a>                </li>\n" +
            "                <li>\n" +
            "                  <a data-name=\"facebook\" href=\"javascript:void(function(){var d=document,e=encodeURIComponent,r=&#39;https://www.facebook.com/dialog/share?app_id=483126645039390&amp;display=popup&amp;href=http://www.jianshu.com/p/03118c11c199&#39;,x=function(){if(!window.open(r,&#39;facebook&#39;,&#39;toolbar=0,resizable=1,scrollbars=yes,status=1,width=450,height=330&#39;))location.href=r};if(/Firefox/.test(navigator.userAgent)){setTimeout(x,0)}else{x()}})();\">\n" +
            "                    <img src=\"http://baijii-common.b0.upaiyun.com/social_icons/32x32/facebook.png\" alt=\"Facebook\" /> 分享到Facebook\n" +
            "</a>                </li>\n" +
            "                <li>\n" +
            "                  <a data-name=\"google_plus\" href=\"javascript:void(function(){var d=document,e=encodeURIComponent,r=&#39;https://plus.google.com/share?url=&#39;+e(&#39;http://www.jianshu.com/p/03118c11c199&#39;),x=function(){if(!window.open(r,&#39;google_plus&#39;,&#39;toolbar=0,resizable=1,scrollbars=yes,status=1,width=450,height=330&#39;))location.href=r};if(/Firefox/.test(navigator.userAgent)){setTimeout(x,0)}else{x()}})();\">\n" +
            "                    <img src=\"http://baijii-common.b0.upaiyun.com/social_icons/32x32/google_plus.png\" alt=\"Google plus\" /> 分享到Google+\n" +
            "</a>                </li>\n" +
            "                <li>\n" +
            "                  <a data-name=\"douban\" href=\"javascript:void(function(){var d=document,e=encodeURIComponent,s1=window.getSelection,s2=d.getSelection,s3=d.selection,s=s1?s1():s2?s2():s3?s3.createRange().text:&#39;&#39;,r=&#39;http://www.douban.com/recommend/?url=&#39;+e(&#39;http://www.jianshu.com/p/03118c11c199&#39;)+&#39;&amp;title=&#39;+e(&#39;在Android Studio中进行单元测试和UI测试&#39;)+&#39;&amp;sel=&#39;+e(s)+&#39;&amp;v=1&#39;,x=function(){if(!window.open(r,&#39;douban&#39;,&#39;toolbar=0,resizable=1,scrollbars=yes,status=1,width=450,height=330&#39;))location.href=r+&#39;&amp;r=1&#39;};if(/Firefox/.test(navigator.userAgent)){setTimeout(x,0)}else{x()}})()\">\n" +
            "                    <img src=\"http://baijii-common.b0.upaiyun.com/social_icons/32x32/douban.png\" alt=\"Douban\" /> 分享到豆瓣\n" +
            "</a>                </li>\n" +
            "            </ul>\n" +
            "          </div>\n" +
            "        </div>\n" +
            "      </div>\n" +
            "\n" +
            "      <!-- Modals -->\n" +
            "      <div id=\"share-weixin-modal\" class=\"modal hide fade share-weixin-modal\" tabindex=\"-1\" role=\"dialog\" aria-labelledby=\"myModalLabel\" aria-hidden=\"true\">\n" +
            "  <div class=\"modal-header\">\n" +
            "    <button type=\"button\" class=\"close\" data-dismiss=\"modal\" aria-hidden=\"true\">×</button>\n" +
            "  </div>\n" +
            "  <div class=\"modal-body\">\n" +
            "    <h5>打开微信“扫一扫”，打开网页后点击屏幕右上角分享按钮</h5>\n" +
            "  </div>\n" +
            "</div>\n" +
            "\n" +
            "      \n" +
            "      <div id=\"notebooks-menu\" class=\"panel notebooks-menu arrow-top modal hide fade\"><img class=\"loader-tiny\" src=\"http://baijii-common.b0.upaiyun.com/loaders/tiny.gif\" alt=\"Tiny\" /></div>\n" +
            "      <div id=\"collection-menu\" class=\"panel collection-menu arrow-top modal hide fade\"><img class=\"loader-tiny\" src=\"http://baijii-common.b0.upaiyun.com/loaders/tiny.gif\" alt=\"Tiny\" /></div>\n" +
            "      \n" +
            "      <div id=\"likes-modal\" class=\"modal modal_new support_list-modal fullscreen hide fade\" tabindex=\"-1\" role=\"dialog\" aria-hidden=\"true\">\n" +
            "  <div class=\"modal-dialog\">\n" +
            "    <div class=\"modal-content\">\n" +
            "      <div class=\"modal-header\">\n" +
            "        <button type=\"button\" class=\"close\" data-dismiss=\"modal\" aria-hidden=\"true\">×</button>\n" +
            "        <h3>喜欢的用户</h3>\n" +
            "      </div>\n" +
            "      <div class=\"modal-body\">\n" +
            "        <ul class=\"unstyled users\">\n" +
            "        </ul>\n" +
            "      </div>\n" +
            "    </div>\n" +
            "  </div>\n" +
            "</div>\n" +
            "\n" +
            "\n" +
            "\n" +
            "    <!-- Comments -->\n" +
            "    <div id=\"comments\" class=\"comment-list clearfix\">\n" +
            "        <div class=\"comment-head clearfix\">\n" +
            "          52条评论\n" +
            "          <span class=\"order\">\n" +
            "            （\n" +
            "            <a data-order=\"asc\" class=\"active\" href=\"javascript:void(0)\">按时间正序</a>·\n" +
            "            <a data-order=\"desc\" href=\"javascript:void(0)\">按时间倒序</a>·\n" +
            "            <a data-order=\"likes_count\" href=\"javascript:void(0)\">按喜欢排序</a>\n" +
            "            ）\n" +
            "          </span>\n" +
            "            <a class=\"goto-comment pull-right\" data-signin-link=\"true\" data-toggle=\"modal\" href=\"/sign_in\">\n" +
            "              <i class=\"fa fa-pencil\"></i>添加新评论\n" +
            "</a>        </div>\n" +
            "        <div id=\"comment-list\">\n" +
            "          <img class=\"loader-tiny\" src=\"http://baijii-common.b0.upaiyun.com/loaders/tiny.gif\" alt=\"Tiny\" />\n" +
            "        </div>\n" +
            "          <p class=\"signout-comment\">\n" +
            "    <a class=\"btn btn-info\" data-signin-link=\"true\" data-toggle=\"modal\" href=\"/sign_in\">\n" +
            "      登录后发表评论\n" +
            "</a>  </p>\n" +
            "\n" +
            "    </div>\n" +
            "    <!-- -->\n" +
            "\n" +
            "  </div>\n" +
            "\n" +
            "  <script type='application/json' data-name='note'>\n" +
            "    {\"id\":1539608,\"abbr\":\"本篇教程翻译自GoogleI/O2015中关于测试的codelab，掌握科学上网的同学请点击这里阅读：UnitandUITestinginAndroidStudio。能力有限，如有翻译错误，请批评指正。如需转载，请注明出处。Github下载测试源码目录...\",\"slug\":\"03118c11c199\",\"url\":\"http://www.jianshu.com/p/03118c11c199\",\"image_url\":\"http://cwb.assets.jianshu.io/notes/images/1539608/weibo/image_0dab2954331f.jpg\",\"wordage\":3001,\"has_further_readings\":false,\"views_count\":46110,\"likes_count\":186,\"comments_count\":52,\"rewards_total_count\":4}\n" +
            "  </script>\n" +
            "\n" +
            "  <script type='application/json' data-name='uuid'>\n" +
            "    {\"uuid\":\"68f01e78-a0c1-42e5-b6b7-75cf16975d1e\"}\n" +
            "  </script>\n" +
            "\n" +
            "  <script type='application/json' data-name='author'>\n" +
            "    {\"id\":580359,\"nickname\":\"TestDevTalk\",\"slug\":\"173dd2126629\",\"public_notes_count\":17,\"followers_count\":436,\"total_likes_count\":289,\"is_current_user\":false,\"is_signed_author\":false}\n" +
            "  </script>\n" +
            "\n" +
            "\n" +
            "  <div class=\"include-collection\">\n" +
            "    <div class=\"content\">\n" +
            "\n" +
            "      \n" +
            "        <h5>被以下专题收入，发现更多相似内容：</h5>\n" +
            "        <ul id=\"all-collections\" class=\"unstyled collections-list\">\n" +
            "            <li>\n" +
            "              <a class=\"avatar\" href=\"/collection/3fde3b545a35\"><img src=\"http://upload.jianshu.io/collections/images/284/android.graphics.Bitmap_3d178cba.jpeg?imageMogr2/auto-orient/strip|imageView2/2/w/180\" alt=\"180\" /></a>              <div class=\"collections-info\">\n" +
            "                <h5>\n" +
            "                  <a href=\"/collection/3fde3b545a35\">Android知识</a>\n" +
            "                </h5>\n" +
            "                \n" +
            "  <div class=\"btn btn-success follow\">\n" +
            "    <a data-signin-link=\"true\" data-toggle=\"modal\" href=\"/sign_in\"><i class=\"fa fa-fw fa-plus\"></i>  <span>添加关注</span></a>\n" +
            "    <span>23.6K</span>\n" +
            "  </div>\n" +
            "\n" +
            "                <p class=\"description\">分享Android开发的知识，教程，解析，前沿信息，都可以，欢迎大家投稿~\n" +
            "内容可搞笑，可逗比，另外欢迎申请管理员\n" +
            "</p>\n" +
            "                <p>\n" +
            "                  <a class=\"blue-link\" href=\"/collection/3fde3b545a35\">6930篇文章</a> · 23680人关注\n" +
            "                </p>\n" +
            "              </div>\n" +
            "            </li>\n" +
            "            <li>\n" +
            "              <a class=\"avatar\" href=\"/collection/6e31b7354a48\"><img src=\"http://upload.jianshu.io/collections/images/1776/android-demo.png?imageMogr2/auto-orient/strip|imageView2/2/w/180\" alt=\"180\" /></a>              <div class=\"collections-info\">\n" +
            "                <h5>\n" +
            "                  <a href=\"/collection/6e31b7354a48\">Android Dev</a>\n" +
            "                </h5>\n" +
            "                \n" +
            "  <div class=\"btn btn-success follow\">\n" +
            "    <a data-signin-link=\"true\" data-toggle=\"modal\" href=\"/sign_in\"><i class=\"fa fa-fw fa-plus\"></i>  <span>添加关注</span></a>\n" +
            "    <span>5.1K</span>\n" +
            "  </div>\n" +
            "\n" +
            "                <p class=\"description\">欢迎与我交流Android 开发和互联网项环内容。\n" +
            "欢迎关注我的微信公众号：AndroidMate\n" +
            "</p>\n" +
            "                <p>\n" +
            "                  <a class=\"blue-link\" href=\"/collection/6e31b7354a48\">208篇文章</a> · 5146人关注\n" +
            "                </p>\n" +
            "              </div>\n" +
            "            </li>\n" +
            "            <li>\n" +
            "              <a class=\"avatar\" href=\"/collection/acd3638cc950\"><img src=\"http://upload.jianshu.io/collections/images/6824/1eeef3c955646f09ec9b6777d7eb4db1.jpg?imageMogr2/auto-orient/strip|imageView2/2/w/180\" alt=\"180\" /></a>              <div class=\"collections-info\">\n" +
            "                <h5>\n" +
            "                  <a href=\"/collection/acd3638cc950\">技术文</a>\n" +
            "                </h5>\n" +
            "                \n" +
            "  <div class=\"btn btn-success follow\">\n" +
            "    <a data-signin-link=\"true\" data-toggle=\"modal\" href=\"/sign_in\"><i class=\"fa fa-fw fa-plus\"></i>  <span>添加关注</span></a>\n" +
            "    <span>3.6K</span>\n" +
            "  </div>\n" +
            "\n" +
            "                <p class=\"description\">干货技术文。</p>\n" +
            "                <p>\n" +
            "                  <a class=\"blue-link\" href=\"/collection/acd3638cc950\">2087篇文章</a> · 3671人关注\n" +
            "                </p>\n" +
            "              </div>\n" +
            "            </li>\n" +
            "        </ul>\n" +
            "    </div>\n" +
            "  </div>\n" +
            "\n" +
            "  <!-- Modal -->\n" +
            "    <div class=\"modal pay-modal text-center hide fade\" id=\"pay-modal\">\n" +
            "  <div class=\"modal-header clearfix\">\n" +
            "    <button type=\"button\" class=\"close\" data-dismiss=\"modal\" aria-hidden=\"true\">&times;</button>\n" +
            "  </div>\n" +
            "  <form id=\"new_reward\" class=\"new_reward\" target=\"_blank\" action=\"/notes/1539608/rewards\" accept-charset=\"UTF-8\" method=\"post\"><input name=\"utf8\" type=\"hidden\" value=\"&#x2713;\" /><input type=\"hidden\" name=\"authenticity_token\" value=\"r4T6auXjpfrPy8nnL1vp0xpgk0roCnUVjr4M/3dFlXOH2aoffv0LBgM6fV47AB3LK8iOm6iEyFJJ4XY4DwP5WQ==\" />\n" +
            "    <div class=\"modal-body\">\n" +
            "      <a href=\"/users/173dd2126629\">\n" +
            "        <div class=\"avatar\"><img src=\"http://upload.jianshu.io/users/upload_avatars/580359/dff73e1a5c40.jpg?imageMogr/thumbnail/90x90/quality/100\" alt=\"100\" /></div>\n" +
            "</a>      <p><i class=\"fa fa-quote-left pull-left\"></i>如果觉得我的文章对您有用，请随意打赏。您的支持将鼓励我继续创作！<i class=\"fa fa-quote-right pull-right\"></i></p>\n" +
            "      <div class=\"main-inputs text-left\">\n" +
            "        <div class=\"control-group\">\n" +
            "          <label for=\"reward_amount\">打赏金额</label><i class=\"fa fa-yen\"></i>\n" +
            "          <input value=\"2.00\" type=\"text\" name=\"reward[amount_in_yuan]\" id=\"reward_amount_in_yuan\" />\n" +
            "        </div>\n" +
            "        <div class=\"control-group message\">\n" +
            "          <textarea placeholder=\"给Ta留言\" name=\"reward[message]\" id=\"reward_message\">\n" +
            "</textarea>\n" +
            "        </div>\n" +
            "      </div>\n" +
            "      <div class=\"choose-pay text-left\">\n" +
            "        <h5>选择支付方式：</h5>\n" +
            "        <div>\n" +
            "          <label for=\"reward_channel_alipay\">\n" +
            "            <input type=\"radio\" value=\"alipay\" checked=\"checked\" name=\"reward[channel]\" id=\"reward_channel_alipay\" />\n" +
            "            <span class=\"alipay-bg\"></span>\n" +
            "</label>\n" +
            "          \n" +
            "          <label for=\"reward_channel_wx_pub_qr\">\n" +
            "            <input type=\"radio\" value=\"wx_pub_qr\" name=\"reward[channel]\" id=\"reward_channel_wx_pub_qr\" />\n" +
            "            微信支付\n" +
            "</label>        </div>\n" +
            "      </div>\n" +
            "    </div>\n" +
            "    <div class=\"modal-footer\">\n" +
            "      <button name=\"button\" type=\"submit\" class=\"btn btn-large btn-pay\">立即支付</button>\n" +
            "    </div>\n" +
            "</form></div>\n" +
            "\n" +
            "    <div class=\"modal success-pay text-center hide fade\" id=\"success-pay\">\n" +
            "  <div class=\"modal-header clearfix\">\n" +
            "    <button type=\"button\" class=\"close\" data-dismiss=\"modal\" aria-hidden=\"true\">&times;</button>\n" +
            "  </div>\n" +
            "  <div class=\"modal-body\">\n" +
            "    <h3><i class=\"icon-ok\"></i>打赏成功</h3>\n" +
            "    <img src=\"http://cdn-qn0.jianshu.io/assets/complete-pay-25426877089eb23bd76d9d0e6aad89c1.png\" alt=\"Complete pay\" />\n" +
            "  </div>\n" +
            "  <div class=\"modal-footer\">\n" +
            "    \n" +
            "  </div>\n" +
            "</div>\n" +
            "\n" +
            "</div>\n" +
            "\n" +
            "  </div>\n" +
            "  <div id=\"flash\" class=\"hide\"></div>\n" +
            "  \n" +
            "  <div id=\"view-mode-modal\" tabindex=\"-1\" class=\"modal hide read-modal\" aria-hidden=\"false\" data-js-module='view-mode-modal'>\n" +
            "    <div class=\"btn-group change-background\" data-toggle=\"buttons-radio\">\n" +
            "      <a class=\"btn btn-daytime active\" data-mode=\"day\" href=\"javascript:void(null);\">\n" +
            "        <i class=\"fa fa-sun-o\"></i>\n" +
            "</a>      <a class=\"btn btn-nighttime \" data-mode=\"night\" href=\"javascript:void(null);\">\n" +
            "        <i class=\"fa fa-moon-o\"></i>\n" +
            "</a>    </div>\n" +
            "    <div class=\"btn-group change-font\" data-toggle=\"buttons-radio\">\n" +
            "      <a class=\"btn font \" data-font=\"font1\" href=\"javascript:void(null);\">宋体</a>\n" +
            "      <a class=\"btn font hei active\" data-font=\"font2\" href=\"javascript:void(null);\">黑体</a>\n" +
            "    </div>\n" +
            "    <div class=\"btn-group change-locale\" data-toggle=\"buttons-radio\">\n" +
            "      <a class=\"btn font active\" data-locale=\"zh-CN\" href=\"javascript:void(null);\">简</a>\n" +
            "      <a class=\"btn font hei \" data-locale=\"zh-TW\" href=\"javascript:void(null);\">繁</a>\n" +
            "    </div>\n" +
            "  </div>\n" +
            "\n" +
            "  \n" +
            "\n" +
            "  <script src=\"http://cdn-qn0.jianshu.io/assets/base-ded41764c207f7ff545c28c670922d25.js\"></script>\n" +
            "  \n" +
            "  <script src=\"http://cdn-qn0.jianshu.io/assets/reading-base-0028299ec0c770293c0f07e2f338b48f.js\"></script>\n" +
            "  <script src=\"http://cdn-qn0.jianshu.io/assets/reading/module_sets/note_show-cc5c20512664aad53acca20308dd4d1c.js\"></script>\n" +
            "  <script>\n" +
            "  (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){\n" +
            "  (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),\n" +
            "  m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)\n" +
            "  })(window,document,'script','https://www.google-analytics.com/analytics.js','ga');\n" +
            "\n" +
            "  ga('create', 'UA-35169517-1', 'auto');\n" +
            "  ga('send', 'pageview');\n" +
            "</script>\n" +
            "\n" +
            "<div style=\"display:none\">\n" +
            "  <script src=\"https://s11.cnzz.com/z_stat.php?id=1258679142&web_id=1258679142\" language=\"JavaScript\"></script>\n" +
            "</div>\n" +
            "\n" +
            "<script>\n" +
            "  (function(){\n" +
            "      var bp = document.createElement('script');\n" +
            "      var curProtocol = window.location.protocol.split(':')[0];\n" +
            "      if (curProtocol === 'https') {\n" +
            "          bp.src = 'https://zz.bdstatic.com/linksubmit/push.js';\n" +
            "      }\n" +
            "      else {\n" +
            "          bp.src = 'http://push.zhanzhang.baidu.com/push.js';\n" +
            "      }\n" +
            "      var s = document.getElementsByTagName(\"script\")[0];\n" +
            "      s.parentNode.insertBefore(bp, s);\n" +
            "  })();\n" +
            "</script>\n" +
            "\n" +
            "</body>\n" +
            "</html>\n";

    private HtmlImgReplaceUtil mHtmlImgReplaceUtil;


    @Before
    public void setUp() throws Exception {
        mHtmlImgReplaceUtil = new HtmlImgReplaceUtil(HTML_1);

    }

    @Test
    public void testUrlUndress() {
        for(String src : mHtmlImgReplaceUtil.getHtmlSrcList()) {
            System.out.println("SRC: " + src);
            System.out.println(HtmlImgReplaceUtil.getNoParameterUrl(src) + ".local_version");
        }
        assertEquals(true, true);
    }

    @Test
    public void workNormal() throws Exception {
        List<String> mockLocalList = new ArrayList<>();
        for(String src : mHtmlImgReplaceUtil.getHtmlSrcList()) {
            System.out.println("SRC: " + src);
            mockLocalList.add(src + ".local_version");
        }
        mHtmlImgReplaceUtil.replaceSrcListWithLocalAddress(mockLocalList);
        System.out.println("\n\n\n");
        for(String src : mHtmlImgReplaceUtil.getHtmlSrcList()) {
            System.out.println("RE_SRC: " + src);
        }
        assertEquals(mHtmlImgReplaceUtil.getHtmlString() != null, true);
    }

}