package com.nishtahir.linkbait.plugin

import com.nishtahir.linkbait.commons.BrowserAction
import com.nishtahir.linkbait.commons.BrowserPoolExecutor
import org.jsoup.Jsoup
import org.openqa.selenium.WebDriver

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

        queryLinkedInForJobs(req, event)
    }


    fun queryLinkedInForJobs(request: JobRequest, event: MessageEvent) {
        val linkedInQuery = buildLinkedInUrl(request)
        BrowserPoolExecutor.execute(object : BrowserAction {
            override fun doOnExecutorPool(driver: WebDriver) {
                context.getMessenger().setTyping(event.channel)
                driver.get(linkedInQuery)

                val pageSource = driver.pageSource
                val document = Jsoup.parse(pageSource)

                val jobListings = mutableListOf<Job>()
                document?.let {
                    it.select(".job-listing").filter {
                        it.select(".date-posted-or-new").text().toLowerCase() == "new"
                    }.map {
                        Job(title = it.select(".job-title-text").joinToString(" ") { elem ->
                            elem.text().trim()
                        },
                                titleUrl = it.select(".job-title-link").attr("abs:href"),
                                company = it.select(".company-name-text").text(),
                                location = it.select(".job-location").text(),
                                datePosted = it.select(".date-posted-or-new").text(),
                                description = it.select(".job-description").text(),
                                source = "LinkedIn")
                    }.toCollection(jobListings)

                    if (jobListings.size == 0) {
                        context.getMessenger().sendMessage(event.channel, "No new jobs in the area.")
                        return
                    } else {
                        val message = jobListings.joinToString("\n") { listing: Job ->
                            "*<${listing.titleUrl}|${listing.title}>*, ${listing.company}, ${listing.location}. \n${listing.description}"
                        }
                        context.getMessenger().sendMessage(event.channel, message)
                    }
                }
            }
        })
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
