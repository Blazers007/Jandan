// 加载样式
function initWithCssType(type){
	// 引入样式文件
	var link = document.getElementById("style");
	if(type == "day"){
		link.setAttribute("href","file:///android_asset/css/day.css");
	}else if(type == "night"){
		link.setAttribute("href","file:///android_asset/css/night.css");
	}

	// 绑定图片监听
	var images = document.getElementsByTagName('img');
    if (images && images.length) {
		for (var i = 0 ; i < images.length ; i ++) {
			images[i].onclick = function(){
				blazers.viewImageBySrc(this.src, this.alt);
			};
		}
	}
}
