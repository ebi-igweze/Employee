/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
var canvas = document.getElementById("myCanvas");
var context = canvas.getContext("2d");
canvas.addEventListener("click", defineImage, false);

function defineImage(evt){
    var currentPos = getCurrentPos(evt);
    
    for(i=0; i < document.inputForm.color.length; i++){
        if(document.inputForm.color[i].checked){
            
        }
    }
}


