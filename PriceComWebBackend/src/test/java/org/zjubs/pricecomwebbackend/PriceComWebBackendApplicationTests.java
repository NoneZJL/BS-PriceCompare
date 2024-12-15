package org.zjubs.pricecomwebbackend;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zjubs.pricecomwebbackend.service.UserService;
import org.zjubs.pricecomwebbackend.utils.EmailUtil;

import java.util.Date;

@SpringBootTest
class PriceComWebBackendApplicationTests {

	@Autowired
	private UserService userService;
	@Autowired
	private EmailUtil emailUtil;

 	@Test
	void contextLoads() {
	}

//	@Test
//	public void testGenJwt() {
//		Map<String, Object> claims = new HashMap<>();
//		claims.put("id", 1);
//		claims.put("name", "tom");
//
//		String jwt = Jwts.builder().signWith(SignatureAlgorithm.HS256, "BS").setClaims(claims).setExpiration(new Date(System.currentTimeMillis() + 3600 * 1000)).compact();
//
//		System.out.println(jwt);
//	}

	@Test
	public void testJwt() {
		String token = JWT.create()
				.withClaim("id", 1)
				.withClaim("username", "tom")
				.withExpiresAt(new Date(System.currentTimeMillis() + 3600 * 1000))
				.sign(Algorithm.HMAC256("ZJUBS"));

		System.out.println(token);

		JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256("ZJUBS")).build();
		DecodedJWT decodesJWT = jwtVerifier.verify(token);
		Integer id = decodesJWT.getClaim("id").asInt();
		String username = decodesJWT.getClaim("username").asString();
		Date expiresAt = decodesJWT.getExpiresAt();
		System.out.println("id = " + id + ", username = " + username + ", end_time = " + expiresAt);
	}

	@Test
	public void sendEmail() {
//		userService.sendEmailJustifyCode("1445675246@qq.com");
		userService.sendEmailJustifyCode("1290217090@qq.com");
	}

	@Test
	public void sendReminderEmail() {
		emailUtil.sendRemainderEmail("1290217090@qq.com","Macbook", 18888.0, 14999.0,"https://item.jd.com/100100209772.html" );
	}
}
