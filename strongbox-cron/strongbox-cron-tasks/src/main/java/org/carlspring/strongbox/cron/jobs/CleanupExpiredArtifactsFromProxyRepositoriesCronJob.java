package org.carlspring.strongbox.cron.jobs;

import org.carlspring.strongbox.cron.domain.CronTaskConfigurationDto;
import org.carlspring.strongbox.cron.jobs.properties.*;
import org.carlspring.strongbox.providers.repository.proxied.LocalStorageProxyRepositoryExpiredArtifactsCleaner;

import javax.inject.Inject;
import java.util.List;

import edu.emory.mathcs.backport.java.util.Arrays;

/**
 * @author Przemyslaw Fusik
 */
public class CleanupExpiredArtifactsFromProxyRepositoriesCronJob
        extends JavaCronJob
{

    @Inject
    private LocalStorageProxyRepositoryExpiredArtifactsCleaner proxyRepositoryObsoleteArtifactsCleaner;

    @Override
    public void executeTask(final CronTaskConfigurationDto config)
            throws Throwable
    {
        final String lastAccessedTimeInDaysText = config.getRequiredProperty("lastAccessedTimeInDays");
        final String minSizeInBytesText = config.getProperty("minSizeInBytes");

        final Integer lastAccessedTimeInDays;
        try
        {
            lastAccessedTimeInDays = Integer.valueOf(lastAccessedTimeInDaysText);
        }
        catch (NumberFormatException ex)
        {
            logger.error("Invalid integer value [" + lastAccessedTimeInDaysText +
                         "] of 'lastAccessedTimeInDays' property. Cron job won't be fired.", ex);
            return;
        }

        Long minSizeInBytes = Long.valueOf(-1);
        if (minSizeInBytesText != null)
        {
            try
            {
                minSizeInBytes = Long.valueOf(minSizeInBytesText);
            }
            catch (NumberFormatException ex)
            {
                logger.error("Invalid Long value [" + minSizeInBytesText +
                             "] of 'minSizeInBytes' property. Cron job won't be fired.", ex);
                return;
            }
        }

        proxyRepositoryObsoleteArtifactsCleaner.cleanup(lastAccessedTimeInDays, minSizeInBytes);
    }

    @Override
    public List<CronJobProperty> getProperties()
    {
        return Arrays.asList(new CronJobProperty[]{
                new CronJobIntegerTypeProperty(
                        new CronJobRequiredProperty(new CronJobNamedProperty("lastAccessedTimeInDays"))),
                new CronJobIntegerTypeProperty(
                        new CronJobOptionalProperty(new CronJobNamedProperty("minSizeInBytes"))) });
    }

}
