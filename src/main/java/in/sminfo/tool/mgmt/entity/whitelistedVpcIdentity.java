package in.sminfo.tool.mgmt.entity;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import lombok.Data;

@Embeddable
@Data
public class whitelistedVpcIdentity implements Serializable {
    @NotNull
    private Integer AwsAccountId;

    @NotNull
    private String VpcId;

    public whitelistedVpcIdentity() {

    }

    public whitelistedVpcIdentity(Integer AwsAccountId, String VpcId) {
        this.AwsAccountId = AwsAccountId;
        this.VpcId = VpcId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        whitelistedVpcIdentity that = (whitelistedVpcIdentity) o;

        if (!AwsAccountId.equals(that.AwsAccountId)) return false;
        return VpcId.equals(that.VpcId);
    }

    @Override
    public int hashCode() {
        int result = AwsAccountId.hashCode();
        result = 31 * result + VpcId.hashCode();
        return result;
    }
}