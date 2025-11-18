package services;

import java.util.ArrayList;
import java.util.List;
import models.*;

/**
 * Creation and decision flow for withdrawal requests.
 * <p>This class is documented for API generation via the Javadoc tool.</p>
 */
public class WithdrawalService {
    private List<WithdrawalRequest> requests = new ArrayList<>();
/**
 * method operation.
 *
 * @return result of the operation
 * @throws IllegalArgumentException if invalid arguments
 */

    public void submit(WithdrawalRequest r) { 
        requests.add(r); 
    }
    public List<WithdrawalRequest> getAll() { 
        return requests; 
    }
    public WithdrawalRequest findById(String id) {
        for (WithdrawalRequest r : requests) if (r.getId().equals(id)) return r;
        return null;
    }
    public void addRequest(WithdrawalRequest req) {
        requests.add(req);
    }
}