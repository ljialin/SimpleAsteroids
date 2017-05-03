package adjustable;

import java.util.List;

/**
 * Created by sml on 25/10/2016.
 */


public interface Adjustable {
    List<Parameter> getParameters();
    void setParameters(List<Parameter> params);
}
