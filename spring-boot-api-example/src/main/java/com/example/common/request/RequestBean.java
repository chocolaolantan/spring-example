package com.example.common.request;

import java.util.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 本APIへアクセスする際、すべてでこれを継承する.
 * @author chocolaolantan
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
public class RequestBean
{
	/** リクエストメソッド. */
	private String method;
	/** ユーザエージェント. */
	private String userAgent;
	/** アクセスクライアント. */
	private String client;
	/** リクエスト日時. */
	private Date receptionDate;
}
