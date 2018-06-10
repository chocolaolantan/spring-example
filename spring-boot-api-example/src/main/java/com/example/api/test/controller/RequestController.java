package com.example.api.test.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.api.test.bean.TestRequest;
import com.example.api.test.bean.TestResponse;
import com.example.common.util.RequestResponseUtils;
import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.extern.log4j.Log4j2;

/**
 * 動作確認用コントローラ
 * @author chocolaolantan
 */
@RestController
@RequestMapping( value = "/test/request")
@Log4j2
public class RequestController
{
	/**
	 * GETリクエストの動作確認.
	 * @param request
	 * @return
	 * @throws JsonProcessingException
	 */
	@RequestMapping( value = "/get", method = RequestMethod.GET,  produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
	public TestResponse get( TestRequest request )
	{
		log.debug( "request : {}", RequestResponseUtils.toJson( request ) );

		TestResponse response = new TestResponse();
		response.setText( "request : " + RequestResponseUtils.toJson( request ) );

		log.debug( "response : {}", RequestResponseUtils.toJson( response ) );

		return response;
	}

	/**
	 * POSTリクエストの動作確認.
	 * @param request
	 * @return
	 * @throws JsonProcessingException
	 */
	@RequestMapping( value = "/post", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
	public TestResponse post( TestRequest request )
	{
		log.debug( "request : {}", RequestResponseUtils.toJson( request ) );

		TestResponse response = new TestResponse();
		response.setText( "request : " + RequestResponseUtils.toJson( request ) );

		log.debug( "response : {}", RequestResponseUtils.toJson( response ) );

		return response;
	}
}
