package org.webcat.reporter;

import org.webcat.jobqueue.JobBase;
import org.webcat.jobqueue.ManagedJobBase;

public class ManagedReportGenerationJob extends ManagedJobBase
{
    public ManagedReportGenerationJob(ReportGenerationJob job)
    {
        super(job);
    }


    // ----------------------------------------------------------
    /**
     * Retrieve the entity pointed to by the <code>generatedReport</code>
     * relationship.
     * @return the entity in the relationship
     */
    public GeneratedReport generatedReport()
    {
        return (GeneratedReport)valueForKey(
                ReportGenerationJob.GENERATED_REPORT_KEY);
    }


    // ----------------------------------------------------------
    /**
     * Set the entity pointed to by the <code>generatedReport</code>
     * relationship.  This method is a type-safe version of
     * <code>addObjectToBothSidesOfRelationshipWithKey()</code>.
     *
     * @param value The new entity to relate to
     */
    public void setGeneratedReportRelationship(GeneratedReport value)
    {
        addObjectToBothSidesOfRelationshipWithKey(value,
                ReportGenerationJob.GENERATED_REPORT_KEY);
    }
}
