dojo.declare("webcat.jobqueue.JobProgressWatcher", null,
{
    pageRPC: null,


    // ----------------------------------------------------------
    constructor: function(args)
    {
        dojo.mixin(this, args);

        this._started = false;
        this._isComplete = false;
    },


    // ----------------------------------------------------------
    start: function()
    {
        if (!this._started)
        {
            this.pageRPC.pollJobStatus(dojo.hitch(this, this._update));

            this._interval = setInterval(dojo.hitch(this, function() {
                this.pageRPC.pollJobStatus(dojo.hitch(this, this._update));
            }), 5000);

            this._started = true;
        }
    },


    // ----------------------------------------------------------
    stop: function()
    {
        if (this._interval)
        {
            this._started = false;
            clearInterval(this._interval);
            delete this._interval;
        }
    },


    // ----------------------------------------------------------
    _update: function(status, e)
    {
        if (!status)
        {
            alert("A serious error occurred while communicating with the "
                    + "Web-CAT server:\n\n" + e);
            this.stop();
            return;
        }

        if (status.isComplete || status.readyStateDidChange)
        {
            this.stop();
            JobProgressPageWrapper_reloadPage();
        }
        else
        {
            var progressBar = dijit.byId('JobProgressPageWrapper_progressBar');
            progressBar.attr('value', status.progress);

            var progressMessage = dojo.byId(
                    'JobProgressPageWrapper_progressMessage');

            if (status.isStarted)
            {
                progressMessage.innerText =
                    status.progressMessage || "Running job...";
            }
            else
            {
                progressMessage.innerText = "This job has not yet started. It "
                    + "is at position " + status.queuePosition + " in the "
                    + "queue.";
            }
        }
    }
});
