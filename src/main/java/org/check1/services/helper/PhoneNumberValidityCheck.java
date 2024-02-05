package org.check1.services;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Service
@NoArgsConstructor
@Getter
@Setter
public class PhoneNumberValidityCheck {
    public Boolean check(String phoneNumber)
    {
        if(phoneNumber.length()!=13)
        {
            return false;
        }
        if(phoneNumber.charAt(0)!='+')
        {
            return false;
        }
        if(phoneNumber.charAt(1)!='9' || phoneNumber.charAt(2)!='1')
            return false;
        if(phoneNumber.charAt(3)>='6' && phoneNumber.charAt(3)<='9')
        {
            for(int i=4;i<13;i++) {
                if (phoneNumber.charAt(i) >= '0' && phoneNumber.charAt(i) <= '9')
                    continue;
                else
                    return false;
            }
            return true;
        }
        else return false;

    }
}
