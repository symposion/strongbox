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

    private static final String PROPERTY_LAST_ACCESSED_TIME_IN_DAYS = "lastAccessedTimeInDays";

    private static final String PROPERTY_MIN_SIZE_IN_BYTES = "minSizeInBytes";

    private static final List<CronJobProperty> PROPERTIES = Arrays.asList(new CronJobProperty[]{
            new CronJobIntegerTypeProperty(
                    new CronJobRequiredProperty(new CronJobNamedProperty(PROPERTY_LAST_ACCESSED_TIME_IN_DAYS))),
            new CronJobIntegerTypeProperty(
                    new CronJobOptionalProperty(new CronJobNamedProperty(PROPERTY_MIN_SIZE_IN_BYTES))) });

    @Inject
    private LocalStorageProxyRepositoryExpiredArtifactsCleaner proxyRepositoryObsoleteArtifactsCleaner;

    @Override
    public void executeTask(final CronTaskConfigurationDto config)
            throws Throwable
    {
        final String lastAccessedTimeInDaysText = config.getRequiredProperty(PROPERTY_LAST_ACCESSED_TIME_IN_DAYS);
        final String minSizeInBytesText = config.getProperty(PROPERTY_MIN_SIZE_IN_BYTES);

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
        return PROPERTIES;
    }

    @Override
    public String getName()
    {
        return "Cleanup Expired Artifacts From Proxy Repositories Cron Job";
    }

    @Override
    public String getDescription()
    {
        return "Cleanup Expired Artifacts From Proxy Repositories Cron Job";
    }

}
