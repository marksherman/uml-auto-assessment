dojo.declare("webcat.reporter.GeneratedReportWatcher", null,
{
    // ----------------------------------------------------------
    constructor: function(pageRPC, pageContainerID)
    {
        this._pageRPC = pageRPC;
        this._currentPage = 0;
        this._pagesSoFar = 0;
        this._isComplete = false;
        this._hasErrors = false;
        this._pageContainerID = pageContainerID;
    },


    // ----------------------------------------------------------
    initializeFromCompleteReport: function(numPages, hasErrors)
    {
        this._currentPage = 1;
        this._pagesSoFar = numPages;
        this._isComplete = true;
        this._hasErrors = hasErrors;

        if (hasErrors)
        {
            dojo.style("errorNotification", "display", "block");
        }
    },


    // ----------------------------------------------------------
    start: function()
    {
        dojo.style("progressArea", "display", "block");

        this._interval = setInterval(dojo.hitch(this, function() {
            this._pageRPC.pollReportStatus(
                dojo.hitch(this, this._pollReportStatusCallback));
        }), 5000);
    },


    // ----------------------------------------------------------
    _pollReportStatusCallback: function(result, e)
    {
        if (!result) return;

        dijit.byId("reportProgressBar").update({
            'progress': result.progress
        });

        this._isStarted = result.isStarted;
        this._isCanceled = result.isCanceled;
        this._queuePosition = result.queuePosition;
        this._isComplete = result.isComplete;
        this._hasErrors = result.hasErrors;
        this._pagesSoFar = result.highestRenderedPageNumber;

        var messageNode = dojo.byId("progressMessage");

        if (this._isCanceled)
        {
            messageNode.innerHTML = "<p>You have <b>canceled</b> the " +
                "generation of this report.</p>";

            this.stop();
            dojo.style("progressArea", "display", "none");

            return;
        }
        else if (this._isStarted)
        {
            messageNode.innerHTML = "<p>Your report is currently " +
                "being generated. As pages of the report become " +
                "available, you can view them below.</p>";
        }
        else
        {
            messageNode.innerHTML = "<p>Your report is currently in " +
                "position " + this._queuePosition + " in the queue." +
                "</p>";
        }

        if (this._pagesSoFar == 0 && result.highestRenderedPageNumber > 0)
        {
            // If the first page just got rendered for the first time,
            // go ahead and force-refresh the content pane so that it
            // gets displayed to the user.

            this._currentPage = 1;
            this._loadPage();
            this._updatePageIndicator();
        }
        else
        {
            this._updatePageIndicator();
        }

        if (result.isComplete || result.hasErrors)
        {
            this._currentPage = 1;
            this._loadPage();
            this._updatePageIndicator();

            this.stop();
            dojo.style("progressArea", "display", "none");

            if (result.hasErrors)
            {
                dojo.style("errorNotification", "display", "block");
            }
        }
    },


    // ----------------------------------------------------------
    _loadPage: function()
    {
        this._pageRPC.setCurrentPageNumber(dojo.hitch(this, function()
        {
            dijit.byId("reportPageContainer").refresh();
            this._updatePageIndicator();
        }), this._currentPage);
    },


    // ----------------------------------------------------------
    _updatePageIndicator: function()
    {
        if (!this._pagesSoFar)
        {
            dojo.byId("pageIndicator").innerHTML = "No pages available yet";
        }
        else
        {
            var pageIndicator = "Page "
               + this._currentPage + " (of " + this._pagesSoFar;

            pageIndicator += (this._isComplete == true) ?
                " total)" : " so far)";
            dojo.byId("pageIndicator").innerHTML = pageIndicator;
        }

        if (this._isComplete)
        {
            // Update the popup menu so that the user can save the report.
            dijit.byId("saveDialogContainer").refresh();
        }

        if (this._hasErrors)
        {
           dijit.byId("errorBlock").refresh();
        }
    },


    // ----------------------------------------------------------
    stop: function()
    {
        clearInterval(this._interval);
        delete this._interval;
    },


    // ----------------------------------------------------------
    goToFirstPage: function()
    {
        if (this._pagesSoFar == 0) return;

        this._currentPage = 1;
        this._loadPage();
    },


    // ----------------------------------------------------------
    goToPreviousPage: function()
    {
        if (this._pagesSoFar == 0) return;

        if (this._currentPage > 1)
            this._currentPage--;

        this._loadPage();
    },


    // ----------------------------------------------------------
    goToNextPage: function()
    {
        if (this._pagesSoFar == 0) return;

        if (this._currentPage < this._pagesSoFar)
            this._currentPage++;

        this._loadPage();
    },


    // ----------------------------------------------------------
    goToLastPage: function()
    {
        if (this._pagesSoFar == 0) return;

        this._currentPage = this._pagesSoFar;
        this._loadPage();
    },


    // ----------------------------------------------------------
    cancel: function()
    {
        dijit.byId("cancelButton").attr("label", "Canceling...");
        dijit.byId("cancelButton").attr("disabled", true);
        this._pageRPC.cancelReport();
    }
});
