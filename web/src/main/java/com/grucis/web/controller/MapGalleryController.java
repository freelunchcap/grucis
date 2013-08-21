package com.grucis.web.controller;

import java.util.List;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import com.grucis.dev.service.DictionaryModelService;
import com.grucis.dev.service.ExportModelService;
import com.grucis.web.mapper.MapViewMapper;
import com.grucis.web.view.BufferedViewCollection;
import com.grucis.web.view.MapView;
import org.jboss.resteasy.annotations.GZIP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
@Path("api/map")
@Produces("application/json")
public final class MapGalleryController {

  @Autowired
  private DictionaryModelService dictionaryModelService;
  @Autowired
  private ExportModelService exportModelService;
  @Autowired
  private MapViewMapper mapViewMapper;

  @GET
  @Path("/maps")
  public Response getMaps(@QueryParam("start") int start, @QueryParam("limit") int limit) {
    List<MapView> views = mapViewMapper.map(dictionaryModelService.getAllMapEntries());
    int to = start + limit;
    int total = views.size();
    if(to > total) to = total;
    return Response.ok(new BufferedViewCollection<MapView>(total, views.subList(start, to))).build();
  }

  @GET
  @Path("/map/{id}.json")
  @GZIP
  public Response getMap(@PathParam("id") int id) {
    return Response.ok(exportModelService.getTileMap(id, false)).build();
  }

}
