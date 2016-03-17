function getFiles(filesData) {
    for (var i in filesData) {
        var elem = $("<a>");
        elem.attr("href", "files/" + filesData[i].filename);
        elem.text(filesData[i].comment);
        $("#fileList").append(elem);
        var elem2 = $("<br>");
        $("#fileList").append(elem2);

    }
}

function getCounts(countsData) {
    $("#fileCount").append().text("Your repository contains " + countsData.all + " Total files of which " + countsData.perm + " are permanent.");
}

$.get("/files", getFiles);
$.get("/counts", getCounts);