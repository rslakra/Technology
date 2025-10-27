'use strict';

var uploadImagesForm = document.querySelector('#uploadImagesForm');
var uploadImagesInput = document.querySelector('#uploadImagesInput');
var uploadImagesError = document.querySelector('#uploadImagesError');
var uploadImagesSuccess = document.querySelector('#uploadImagesSuccess');

function uploadMultipleImages(files) {
    var formData = new FormData();
    for(var index = 0; index < files.length; index++) {
        formData.append("files", files[index]);
    }

    var xhr = new XMLHttpRequest();
    xhr.open("POST", "/uploadImages");

    xhr.onload = function() {
        console.log(xhr.responseText);
        var response = JSON.parse(xhr.responseText);
        if(xhr.status == 200) {
            //fill table with data
            fillDOMWithImages(response, true, false);
            var content = '';
            content += response.length;
            content += " File(s) Uploaded Successfully!";
            uploadImagesError.style.display = "none";
            uploadImagesSuccess.innerHTML = content;
            uploadImagesSuccess.style.display = "block";
        } else {
            //fill table with data
            uploadImagesSuccess.style.display = "none";
            uploadImagesError.innerHTML = (response && response.message) || "Upload Error!";
        }
    }

    xhr.send(formData);
}

//Add listener on submit button
uploadImagesForm.addEventListener('submit', function(event){
    var files = uploadImagesInput.files;
    if(files.length === 0) {
        uploadImagesError.innerHTML = "Please select a file";
        uploadImagesError.style.display = "block";
    }
    uploadMultipleImages(files);
    event.preventDefault();
}, true);


//once the html is loaded, load all images.
window.onload = function() {
    loadAllImages();
}

//load all images
function loadAllImages() {
    var xhr = new XMLHttpRequest();
    xhr.open("GET", "/loadAllImages", true);

    xhr.onload = function() {
//        console.log(xhr.responseText);
        var response = JSON.parse(xhr.responseText);
        var data = '';
        if(xhr.status == 200) {
            //fill table with data
            fillDOMWithImages(response, true, true);
        } else {
            //fill table with error data
            fillDOMWithImages(response, false, true);
        }
    }

    xhr.send();
}

//load all images
function fillDOMWithImages(response, isSuccess, reload) {
    var listImagesData = document.querySelector('#listImagesData')
    var data = '';
    if(isSuccess && response.length > 0) {
        for (var i = 0; i < response.length; i++) {
            var value = response[i];
            data += '<tr>';
            data += '<td>' + value.title + '</td>';
            data += '<td>' + value.createdOn + '</td>';
            data += '<td>' + value.type + '</td>';
            data += '<td>' + value.size + '</td>';
            if(value.downloadUrl != null)
                data += '<td><a href="' + value.downloadUrl + '">Download</a></td>';
            data += '</tr>';
        }
    } else {
        if(response && response.message) {
            data += '<td colspan="5">';
            data += (response && response.message) || "Upload Error!";
        } else {
            data += '<td colspan="5" style="text-align: center">';
            data += 'No Records Available!';
        }
        data += '</td>';
    }

    if(reload){
        listImagesData.innerHTML = data;
    }else {
        if(listImagesData.innerHTML.includes('No Records'))
            listImagesData.innerHTML = data;
        else {
            var allData = '';
            allData += listImagesData.innerHTML;
            if(!data.includes('No Records'))
                allData += data
            listImagesData.innerHTML = allData;
        }
    }

}



