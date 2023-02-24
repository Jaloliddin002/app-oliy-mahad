package project.security.oauth2.user;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public abstract class OAuth2UserInfo {

    protected Map<String, Object> attributes;

    public abstract String getId ();

    public abstract String getEmail();

    public abstract String getName();

    public abstract String getImageUrl();


}
