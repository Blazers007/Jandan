function loadCssFile(type){
	var link = document.getElementById("style");
	if(type == "day"){
		link.setAttribute("href","file:///android_asset/css/day.css");
	}else if(type == "night"){
		link.setAttribute("href","file:///android_asset/css/night.css");
	}
}