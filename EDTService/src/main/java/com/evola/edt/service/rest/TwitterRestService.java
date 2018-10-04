package com.evola.edt.service.rest;

import java.util.List;

import javax.inject.Named;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.impl.TwitterTemplate;

@Path("/twitter")
@Named
public class TwitterRestService extends AbstractRestService  {

	@Value("${twitter.consumer.key}")
	private String consumerKey;

	@Value("${twitter.consumer.secret}")
	private String consumerSecret;
	
	@Value("${twitter.access.token}")
	private String accessToken;

	@Value("${twitter.access.secret}")
	private String accessSecret;
	
	@GET
	@Path("/getTweets")
	@Produces("application/json")
	public List<Tweet> getPage() {

		Twitter twitter = new TwitterTemplate(consumerKey, consumerSecret, accessToken, accessSecret);
		
		List<Tweet> timeline = twitter.timelineOperations().getUserTimeline();
		
		return timeline;
	}
	
	

}
