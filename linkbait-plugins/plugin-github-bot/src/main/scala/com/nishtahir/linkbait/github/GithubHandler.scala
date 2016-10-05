package com.nishtahir.linkbait.github

import com.nishtahir.linkbait.plugin.{Attachment, MessageEvent, MessageEventListener, PluginContext}
import com.github.verbalexpressions.VerbalExpression
import VerbalExpression._
import org.kohsuke.github.{GHRepository, GitHub}
import scala.collection.JavaConverters._

import scala.io.Source


/**
  * Created by nish on 10/4/16.
  */
class GithubHandler(context: PluginContext) extends MessageEventListener {

  val GITHUB_PATTERN =
    $.andThen("http")
      .maybe("s")
      .andThen("://")
      .andThen("github.com/")
      .find($.words(true).or($.digits(true)))
      .andThen("/")
      .find($.words(true).or($.digits(true)))
      .maybe("/")

  override def handleMessageEvent(event: MessageEvent): Unit = {
    val matcher = GITHUB_PATTERN.compile.matcher(event.getMessage)
    if (matcher.find()) {
      val user = matcher.group(5)
      val repo = matcher.group(9)

      val repository = GitHub.connectAnonymously().getRepository(s"$user/$repo")
      if(repository != null){
        val name = repository.getFullName
        val contributors = repository.listContributors().asList().size()
        val titleUrl = repository.getHtmlUrl
        val languages = repository.listLanguages().asScala.keySet.mkString(", ")
        val openIssues = repository.getOpenIssueCount
        val releases = repository.listReleases().asList().size()
        val branches = repository.getBranches.size()
        val commits = repository.listCommits().asList().size()
        val license = repository.getLicense().getName

        val readMe = Source.fromInputStream(repository.getReadme.read()).mkString

        val att = new Attachment(name, readMe, "FFFFFF", titleUrl.toString, "", "")
        att.setAdditionalFields(Map(
          "Languages" -> languages.toString,
          "Open issues" -> openIssues.toString,
          "Contributors" -> contributors.toString,
          "Branches" -> branches.toString,
          "Commits" -> commits.toString,
          "Releases" -> releases.toString,
          "License" -> license.toString
        ).asJava)

        context.getMessenger.sendAttachment(event.getChannel, att)
      }
    }
  }

}
