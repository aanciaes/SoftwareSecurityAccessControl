/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package softwaresecurity.exceptions;

public class UsernameAlreadyExistsException extends Exception {

    private static final long serialVersionUID = 1L;

    public UsernameAlreadyExistsException() {
        super();
    }

    public UsernameAlreadyExistsException(String message) {
        super(message);
    }
}
