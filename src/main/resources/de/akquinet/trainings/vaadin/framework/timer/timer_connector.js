/**
 * @author Axel Meier, akquinet engineering GmbH
 */

window.de_akquinet_trainings_vaadin_framework_timer_TimerExtension = function () {
    var connector = this;

    var enabled;
    var intervalId = null;
    var intervalInMs;
    var started = false;

    var eventCount = 0;
    
    // var parent = connector.getParentId();
    // var element = connector.getElement();
    // console.log("TimerExtension parent:" + parent + ", connector:" + connector, ", element:" + element);

    var stopImpl = function () {
        if (null !== intervalId) {
            clearTimeout(intervalId);
            intervalId = null;
        }
    }

    var restartImpl = function () {
        stopImpl();

        if (!!enabled
            && intervalInMs > 0
            && null === intervalId
            && !!started) {
            intervalId = setTimeout(function () {
                ++eventCount;
                console.log("TimerExtension timeout! eventCount=" + eventCount);
                connector.timeout();
            }, intervalInMs);
        }
    }

    connector.triggerNextInterval = function () {
        restartImpl();
    }

    // connector.stop = function () {
    //     stopImpl()
    // }

    connector.onStateChange = function () {
        enabled = this.getState().enabled;
        intervalInMs = this.getState().intervalInMs;
        started = this.getState().started;
        console.log("onStateChange: enabled=" + enabled
            + ", intervalMs=" + intervalInMs + ", started=" + started);

        if (!enabled
            || !(intervalInMs > 0)
            || !started){
            stopImpl();
        }
    }


};
