package com.nishtahir.islackbot.util;

public class HelpUtils {

	/**
	 * @param context String to match
	 * @param sessionId id of the bot for current session
	 * @return true if valid
	 */
	static boolean isValidHelpRequest(String context, String sessionId) {
		return context.matches(/^(?i)(<@${sessionId}>:?)\s+help(\s?me.*)?\S*?$/)
	}
}
