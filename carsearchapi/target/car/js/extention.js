function F$(){var elements=[];for(var i=0;i<arguments.length;i++){var elem=arguments[i];if(typeof elem=="string"){elem=document.getElementById(elem);}
if(arguments.length==1){return elem;}
elements.push(elem);}
return elements;};function addEvent(elem,type,fn){if(elem.nodeName==="A"&&type==="click"){elem.setAttribute("href","javascript:void(0)")};if(elem.addEventListener){elem.addEventListener(type,fn,false);return true;}else if(elem.attachEvent){elem['e'+type+fn]=fn;elem[type+fn]=function(){elem['e'+type+fn](window.event);}
elem.attachEvent("on"+type,elem[type+fn]);return true;}
return false;};function cpu(t,b,c,d){return c*((t=t/d-1)*t*t+1)+b;};function slowdown(element,theClass,start,space,speed,_f){var count=0;var speed=speed||50;function place(){element.style[theClass]=cpu(count,start,space,speed)+"px";count++;if(count===speed){clearTimeout(command);count=0;element.style[theClass]=space+start+"px";if(_f)_f();return false;}
var command=setTimeout(place,15);}
place();}
function izone(){
var elem, modHeight, showHeight;
elem = F$("izone");
modHeight = elem.scrollHeight;
showHeight = elem.offsetHeight;
//alert(modHeight+" & "+showHeight)
var more = F$("zoneMore");
var myTime;
if(modHeight > showHeight){
more.style.display = "block";
addEvent(more, "click", zoneSwitch);
}
addEvent(elem, "mouseover", function(){ clearTimeout(myTime) });
addEvent(elem, "mouseout", function(){
if(more.className == "zoneClose"){ myTime = setTimeout(zoneSwitch, none) };
});
function zoneSwitch(){
if(more.className == "zoneMore"){
navigator.userAgent.indexOf('Opera')>=0 ? elem.style.height = "auto" : slowdown(elem, "height", showHeight, (modHeight-showHeight), 20);// 兼容opera获取溢出隐藏的所有高度bug做的特殊处理
more.className = "zoneClose";
}else{
navigator.userAgent.indexOf('Opera')>=0 ? elem.style.height = showHeight +"px" : slowdown(elem, "height", modHeight, (showHeight-modHeight), 20);// 兼容opera获取溢出隐藏的所有高度bug做的特殊处理
more.className = "zoneMore";
}
}
//alert(+" & "+);
}
