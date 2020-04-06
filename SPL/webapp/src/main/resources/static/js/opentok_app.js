var sessionId;
var session;
var imgData;
var apiKey;
var obj
var subscriberFuckingObject;

function getContextPath() {
    return window.location.pathname.substring(0, window.location.pathname.indexOf("/",2));
}

function startStreaming(projectId) {
    $.ajax({
        url: getContextPath()+"/streamctrl/start_stream/" + projectId,
        type: "GET",
        data: projectId,
        success: function (data) {
            startStream(data.apiKey, data.sessionId, data.token);
            sessionId = data.sessionId;
            apiKey = data.apiKey;
            console.log(sessionId);
            updateArchive();
        },
        error: function (request, error) {
            console.log(" Can't do because: " + error);
        },
    }).done(function () {
        console.log("Heureka!");
    });
}

function startArchive(checklistId) {
    $.ajax({
        url: getContextPath()+"/streamctrl/" + sessionId + "/rec/"+checklistId,
        type: "POST",
        success: function () {
            console.log(sessionId);
            unhide(checklistId);
        }, error: function (request, error) {
            console.log(" Can't do because: " + error);
        },
    }).done(function () {
        console.log("start works!");
    });
}

function stopArchive(checklistId) {
    $.ajax({
        url: getContextPath()+"/streamctrl/" + sessionId + "/rec/"+checklistId,
        type: "PUT",
        success: function (data) {
            console.log(sessionId);
            updateArchive();

            $("#archiveEntities_container_"+checklistId).append("<a href= " + data.url + " target='_blank'>  <i title=\"show archive\" class=\"fa fa-file-video-o\" ::before></i>&nbsp;</a>");

        },
        error: function (request, error) {
            console.log(" Can't do because: " + error);
        },
    }).done(function () {
        console.log("stop works!");
    });
}

function snapshot(checklistId) {
    imgData = subscriberFuckingObject.getImgData();
  /*  var img = document.createElement("img");
    img.setAttribute("src", "data:image/png;base64," + imgData);
    var imgWin = window.open("about:blank", "Screenshot");
    imgWin.document.write("<body></body>");
    imgWin.document.body.appendChild(img);*/
    $.ajax({
        url: getContextPath()+"/streamctrl/" + sessionId + "/snap/"+checklistId,
        type: "POST",
        data: JSON.stringify(imgData),
        contentType: 'application/json; charset=utf-8',
        dataType: 'json',
        success: function (data) {
            $("#snapshot_container_"+checklistId).append("<a href=\"/attachments/checklist_snapshot/"+data.pictureURL+"\" target='_blank'>" +
                "<i title=\"show snapshot\" class=\"fa fa-image\" ::before></i>&nbsp;</a>");
        }, error: function (request, error) {
            console.log("error: " + error);
        }
    });
}

function updateArchive() {
    $.ajax({
        url: getContextPath()+"/streamctrl/update/",
        type: "PUT",
        success: function () {
        },
        error: function (request, error) {
            console.log(" Can't do because: " + error);
        },
    }).done(function () {
    });
}

function startStream(apiKey, sessionId, token) {
    // Initialize an OpenTok Session object
    session = OT.initSession(apiKey, sessionId);
    // Initialize a Publisher, and place it into the element with id="publisher"

    var publisher = OT.initPublisher('publisher');
    // Attach an event handler for when the session dispatches the 'streamCreated' event.
    session.on('streamCreated', function (event) {
            // This function runs when another client publishes a stream (eg. session.publish())
            // Subscribe to the stream that caused this event, put it inside the DOM element with id='subscribers'
            subscriberFuckingObject = session.subscribe(event.stream, 'subscribers', {

                    insertMode: 'append'
                }

                , function (error) {
                    if (error) {
                        console.error('Failed to subscribe', error);
                    }
                });
        }
    );


    // Connect to the Session using the 'apiKey' of the application and a 'token' for permission
    session.connect(token, function (error) {
        // This function runs when session.connect() asynchronously completes
        // Handle connection errors
        if (error) {
            console.error('Failed to connect', error);
        } else {
            // Publish the publisher we initialzed earlier (this will trigger 'streamCreated' on other
            // clients)
            session.publish(publisher, function (error) {
                if (error) {
                    console.error('Failed to publish', error);
                }
            });
        }
    });
}

function sendAnswer(checklistId) {
    var checklistElement = $("#status_" + checklistId).val();
    var checklistComment = $("#comment_" + checklistId).val();
    var json = {status: checklistElement, comment: checklistComment};
    if (checklistElement != "OK" && checklistComment == "") {
        try {
            throw "Has to be comment ," +
            "if NOK selected";
        } catch
            (err) {
            alert("Has to be comment if NOK or NAN is selected");
        }
    } else {
        $.ajax({
            url: getContextPath()+"/streamctrl/" + checklistId,
            type: "POST",
            data: JSON.stringify(json),
            contentType: 'application/json; charset=utf-8',
            dataType: 'json',
            success: function () {
                console.log("answer has been sended");
            },
            error: function (request, error) {
                console.log(" Can't do because: " + error);

            }
        }).done(function () {
            $("#check_" + checklistId).prop("checked", true);
        });
    }
}

function unhide(checklistId) {
        $("#icon_stop_record_"+checklistId).hide();
}