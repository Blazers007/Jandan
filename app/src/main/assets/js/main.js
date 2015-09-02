function loadCssFile(type){
	// 是否存在link
	if(type == "day"){
		var fileref = document.createElement('link');
		fileref.setAttribute("rel","stylesheet");
		fileref.setAttribute("type","text/css");
		fileref.setAttribute("href","file:///android_asset/css/day.css");
	}else if(type == "night"){
		var fileref = document.createElement('link');
		fileref.setAttribute("rel","stylesheet");
		fileref.setAttribute("type","text/css");
		fileref.setAttribute("href","file:///android_asset/css/night.css");
	}
	if(typeof fileref != "undefined"){
		document.getElementsByTagName("head")[0].appendChild(fileref);
	}
}