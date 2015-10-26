# event-api-search
Internal search API functions for the Helsingborg Event Database

Built using Java on the Lucene API.

This code is not ready for production, so far it is a proof of concept.

##Configuration

No configuration required, data is by default stored in application root/data, this can however be controlled from the
src/main/resources/settings.properties file.


# Consumer end points


## Search

     POST v_0_0_1/event/search

### Client request

    {
      "reference" : "client defined async reference",
      "limit" : 5
      "startIndex" : 10
      "query": { ... }
    }

Where query is an object at the time best described by inspecting class JSONQuerySerialization. Either a standard Lucene
query with fields etc specified, or an ad hoc implementation fitted for this system. 

For example:

    {
      "type" : "event tags",
      "tags" : [ "Teater", "Dunkers kulturhus" ]
    }

Which is the excat same as:

    {
      "type" : "boolean query",
      "clauses" : [
        {
          "occur" : "must",
          "query" : { "type" : "term", "field" : "Event#tag", "value" : "Teater" }
        }, {
          "occur" : "must",
          "query" : { "type" : "term", "field" : "Event#tag", "value" : "Dunkers kulturhus" }
        }
      ]
    }

Thus the search API allows for pretty much any possible query.


### Server response

    {
      "reference" : "client defined async reference",
      "totalNumberOfSearchResults" : 100
      "startIndex" : 10
      "searchResults": [ 1, 2, 3, 5, 8 ]
    }

## Reconstruct event

     GET v_0_0_1/event/reconstruct/{event identity}

Forces the service to request the event from the primary persistence (Wordpress) and reconstruct all associated
documents in the index.

Called upon from the primary persistence when an event has been modified, in order to minimize the amount of time
before a modification is available in the index. Event modifications that are not notified this way will still be
caught and updated, but it might take a bit of time.

Keep connection alive until event has been updated in index, but does not guarantee that it is available for querying
in case of other ongoing reconstructional work on the index.

## Reconstructing index

     GET v_0_0_1/event/reconstruct/all

Forces the service to reconstruct the whole index. This is useful in the case of inconsistency, e.g. if the index
is returning event identities that has been deleted.





# To do

In descending order of priority.

## PrimaryPersistence

There is no implement connection to Wordpress. Only a mockup.
Without this there is no data in the index.

## IndexUpdateManager

Connects to the primary persistence and receive events created or modified since previous call.
Without this there is no data in the index.

## More ad hoc queries

* General text query for all fields, weighted.
* Start-end-time-related queries. How to handle events with no end-time?
* Better location based queries.
* Price-related queries.

## LocalPersistence

Currently using a JSON-file for storing service information. Might have to evolve a little bit to avoid problems.

## EventIndexAnalyzerFactory

The tokenization of text and what not is very simple and contain no features such as stemming etc. This is something
that needs to be considered before added. Language used in text might differ between event, and so on.

## SystemErrorManager

When the ship goes down, you better be ready. All errors are reported to this manager, but rather than passing it to
an service that reports the errors to an administrator, the service sends the errors do the great void of /dev/null.

Local error, warning, info and debug logging is of course however alive and kicking.

## SystemErrorService

A single endpoint for all services, not only search, allows for changing logging solution
without changing the code in the dependent services.

## Geocoding Service

When location is missing coordinates one needs to geocode. A single endpoint for all services, not only search, allows
for changing geocoding provider without changing the code in the dependent services.