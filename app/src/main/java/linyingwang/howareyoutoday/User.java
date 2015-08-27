package linyingwang.howareyoutoday;

import com.parse.ParseClassName;
import com.parse.ParseUser;

@ParseClassName("_User")
public class User extends ParseUser {
	public static final String PARTNER = "partnerUser";
	public static final String GENDER = "gender";
	public static final String PARTNER_NAME = "partnerName";

	public User getPartner() {
		return (User) getParseUser(PARTNER);
	}

	public void setPartner(User partner) {
		put(PARTNER, partner);
	}

	public String getPartnerName() {
		return getString(PARTNER_NAME);
	}

	public void setPartnerName(String partnerName) {
		put(PARTNER_NAME, partnerName);
	}

	public String getGender() {
		return getString(GENDER);
	}

	public void setGender(String gender) {
		put(GENDER, gender);
	}

}
