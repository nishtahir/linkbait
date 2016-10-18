package com.nishtahir.linkbait.github

import com.nishtahir.linkbait.plugin.{Attachment, MessageEvent, MessageEventListener, PluginContext}
import com.github.verbalexpressions.VerbalExpression
import VerbalExpression._
import org.kohsuke.github.{GHRepository, GHUser, GitHub}

import scala.collection.JavaConverters._
import scala.collection.Map
import scala.io.Source


/**
  * Created by nish on 10/4/16.
  */
class GithubHandler(context: PluginContext) extends MessageEventListener {

  val GITHUB_REPO_PATTERN =
    $.startOfLine()
      .andThen("http")
      .maybe("s")
      .andThen("://")
      .andThen("github.com/")
      .find($.words(true).or($.digits(true)))
      .andThen("/")
      .find($.words(true).or($.digits(true)))
      .maybe("/")
      .endOfLine()

  val GITHUB_PROFILE_PATTERN =
    $.startOfLine()
      .andThen("http")
      .maybe("s")
      .andThen("://")
      .andThen("github.com/")
      .find($.words(true).or($.digits(true)))
      .maybe("/")
      .endOfLine()

  override def handleMessageEvent(event: MessageEvent): Unit = {
    val repoMatcher = GITHUB_REPO_PATTERN.compile.matcher(event.getMessage)
    if (repoMatcher.find()) {
      val user = repoMatcher.group(5)
      val repo = repoMatcher.group(9)

      val repository = GitHub.connectAnonymously().getRepository(s"$user/$repo")
      sendRepositoryAttachment(repository, event)
      return
    }

    val profileMatcher = GITHUB_PROFILE_PATTERN.compile.matcher(event.getMessage)
    if (repoMatcher.find()) {
      val userID = profileMatcher.group(5)

      val user = GitHub.connectAnonymously().getUser(userID)
    }
  }

  private def sendRepositoryAttachment(repository: GHRepository, event: MessageEvent): Unit = {
    if (repository == null) { return }

    val name = repository.getFullName
    val titleUrl = repository.getHtmlUrl
    val languages = repository.listLanguages().asScala.keySet.mkString(", ")
    val openIssues = repository.getOpenIssueCount
    val latestReleases = repository.listReleases().asList().get(0)
    val commits = repository.listCommits().asList().size()

    val readMe = Source.fromInputStream(repository.getReadme.read()).mkString

    val att = new Attachment(name, readMe, "FFFFFF", titleUrl.toString, "", "")
    att.setAdditionalFields(Map(
      "Languages" -> languages.toString,
      "Open issues" -> openIssues.toString,
      "Commits" -> commits.toString,
      "Releases" -> latestReleases.toString
    ).asJava)

    context.getMessenger.sendAttachment(event.getChannel, att)
  }

  private def sendUserAttachment(user: GHUser, event: MessageEvent): Unit = {
    if (user == null) { return }

    val name = user.getName
    val blog = user.getBlog
    val avatarUrl = user.getAvatarUrl
    val repositories = user.getRepositories.asScala

    val att = new Attachment(name, blog, "FFFFFF", "", avatarUrl, "")
    att.setAdditionalFields(convertGHRepositories(repositories).asJava)
    context.getMessenger.sendAttachment(event.getChannel, att)
  }

  private def convertGHRepositories(repositories: Map[String, GHRepository]): Map[String, String] = {
    return repositories.flatMap( repository => Map(repository._1 -> createGHRepositoryStringDescription(repository._2)))
  }

  private def createGHRepositoryStringDescription(repository: GHRepository): String = {
    return " Lang : " + repository.getLanguage + " open issue count : " + repository.getOpenIssueCount
  }
}
