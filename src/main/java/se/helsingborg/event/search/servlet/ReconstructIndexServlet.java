package se.helsingborg.event.search.servlet;

import javax.servlet.http.HttpServlet;

/**
 * @author kalle
 * @since 2015-10-25 16:04
 */
public class ReconstructIndexServlet extends HttpServlet {

  // todo: if inconsistency in index is detected, e.g. returning identities that does not exist in primary persistence,
  // todo: then this servlet can be called upon in order to reconstruct the index from scratch.
  // todo: requests sent to this should be ignored while reconstructing, and probably for a while there after

}
