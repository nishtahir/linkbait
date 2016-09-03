package com.nishtahir.linkbait.plugin

import com.machinepublishers.jbrowserdriver.JBrowserDriver
import com.machinepublishers.jbrowserdriver.Settings
import com.machinepublishers.jbrowserdriver.Timezone
import com.machinepublishers.jbrowserdriver.UserAgent
import org.jsoup.Jsoup

class JobSearchHandler(val context: PluginContext) : MessageEventListener {

    override fun handleMessageEvent(event: MessageEvent) {
        if (event.isDirectedAtBot == false || event.message.startsWith("$TRIGGER") == false) {
            return
        }

        val req = parseJobRequest(event.message)
        if (req.hasMissingProperty()) {
            context.getMessenger().sendMessage(event.channel, HELP_MESSAGE)
            return
        }

        val listings = queryLinkedInForJobs(req)
        val newListings = listings.filter {
            it.datePosted.toLowerCase() == "new"
        }

        if (newListings.size == 0) {
            context.getMessenger().sendMessage(event.channel, "No new jobs in the area.")
            return
        } else {
            val message = newListings.joinToString("\n") { listing: Job ->
                "*<${listing.titleUrl}|${listing.title}>*, ${listing.company}, ${listing.location}. \n${listing.description}"
            }
            context.getMessenger().sendMessage(event.channel, message)
        }
    }


    fun queryLinkedInForJobs(request: JobRequest): List<Job> {
        val linkedInQuery = buildLinkedInUrl(request)

        //This is a dependency that can be moved out of here - IOC
        val driver = JBrowserDriver(Settings.builder().
                userAgent(UserAgent.CHROME).
                timezone(Timezone.AMERICA_NEWYORK).build())
        driver.get(linkedInQuery)

        val pageSource = driver.pageSource
        val document = Jsoup.parse(pageSource)

        val jobListings = mutableListOf<Job>()
        document?.let {
            it.select(".job-listing").forEach { listItem ->
                val title = listItem.select(".job-title-text").joinToString(" ") { elem ->
                    elem.text().trim()
                }
                val titleUrl = listItem.select(".job-title-link").attr("abs:href")
                val company = listItem.select(".company-name-text").text()
                val location = listItem.select(".job-location").text()
                val datePosted = listItem.select(".date-posted-or-new").text()
                val description = listItem.select(".job-description").text()

                jobListings.add(Job(title, titleUrl, company, datePosted, location, description, "LinkedIn"))
            }
        }
        driver.quit()
        return jobListings
    }

    fun parseJobRequest(str: String): JobRequest {
        val req = JobRequest()
        PATTERN.matchEntire(str)?.let { matchResult ->
            req.tags = matchResult.groups[1]?.value?.trim()?.split(" ").orEmpty()
            req.location = matchResult.groups[4]?.value.orEmpty()
        }
        return req
    }

    fun buildLinkedInUrl(request: JobRequest): String {
        val keywords = request.tags.joinToString("%20")
        val location = request.location.replace(" ", "%20")
        return "$LINKEDIN_SEARCH_ROUTE?keywords=$keywords&location=$location"
    }
}

class JobRequest {
    var tags = emptyList<String>()

    var location: String = ""

    fun hasMissingProperty(): Boolean {
        return tags.isEmpty() || location.isNullOrBlank()
    }
}

data class Job(val title: String,
               val titleUrl: String,
               val company: String,
               val datePosted: String,
               val location: String,
               val description: String,
               val source: String)
