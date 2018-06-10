package com.example.common.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * 本APIにおいて例外が発生した際はこれを返却する.
 * @author chocolaolantan
 */
@Getter
@AllArgsConstructor
@ToString
public class ExceptionResponseBean extends ResponseBean
{
	/** メッセージ. */
	private String message;
	/** エラーコード. */
	private String errorCode;
}