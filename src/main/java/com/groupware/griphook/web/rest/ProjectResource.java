package com.groupware.griphook.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.google.common.base.Strings;
import com.groupware.griphook.domain.Phase;
import com.groupware.griphook.domain.Task;
import com.groupware.griphook.domain.Project;
import com.groupware.griphook.repository.PhaseRepository;
import com.groupware.griphook.repository.TaskRepository;
import org.apache.commons.lang3.StringUtils;
import com.groupware.griphook.repository.ProjectRepository;
import com.groupware.griphook.web.rest.errors.BadRequestAlertException;
import com.groupware.griphook.web.rest.util.HeaderUtil;
import com.groupware.griphook.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.json.CDL;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import java.nio.charset.Charset;
import java.util.*;

class TOTALS {
    Float PM_TOTAL = 0.0f;
    Float PS_TOTAL = 0.0f;
}

/**
 * REST controller for managing Project.
 */
@RestController
@RequestMapping("/api")
public class ProjectResource {

    private final Logger log = LoggerFactory.getLogger(ProjectResource.class);

    private static final String ENTITY_NAME = "project";

    private final ProjectRepository projectRepository;
    private final PhaseRepository phaseRepository;
    private final TaskRepository taskRepository;


    public ProjectResource(ProjectRepository projectRepository, PhaseRepository phaseRepository, TaskRepository taskRepository) {
        this.projectRepository = projectRepository;
        this.phaseRepository = phaseRepository;
        this.taskRepository = taskRepository;
    }

    /**
     * POST  /projects : Create a new project.
     *
     * @param project the project to create
     * @return the ResponseEntity with status 201 (Created) and with body the new project, or with status 400 (Bad Request) if the project has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/projects")
    @Timed
    public ResponseEntity<Project> createProject(@RequestBody Project project) throws URISyntaxException {
        log.debug("REST request to save Project : {}", project);
        if (project.getId() != null) {
            throw new BadRequestAlertException("A new project cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Project result = projectRepository.save(project);
        return ResponseEntity.created(new URI("/api/projects/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /projects : Updates an existing project.
     *
     * @param project the project to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated project,
     * or with status 400 (Bad Request) if the project is not valid,
     * or with status 500 (Internal Server Error) if the project couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/projects")
    @Timed
    public ResponseEntity<Project> updateProject(@RequestBody Project project) throws URISyntaxException {
        log.debug("REST request to update Project : {}", project);
        if (project.getId() == null) {
            return createProject(project);
        }
        Project result = projectRepository.save(project);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, project.getId().toString()))
            .body(result);
    }

    /**
     * POST  /projects/{id}/clone/{name} : Clone a Project and its associated phases and tasks
     *
     * @param project the project to clone
     * @return the ResponseEntity with status 201 (Created) and with body the new project, or with status 400 (Bad Request) if the project has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/projects/{id}/clone/{name}")
    @Timed
    public ResponseEntity<Project> cloneProject(@PathVariable Long id, @PathVariable String name) throws URISyntaxException {
        log.debug("REST request to clone Project : {}", id);
        Project project = projectRepository.findOne(id);
        if (project ==  null) {
            return ResponseUtil.wrapOrNotFound(Optional.ofNullable(project));
        }
        project.setName(name);
        project.setId(null);
        Project result = projectRepository.save(project);
        List<Phase> filteredPhases = getProjectPhases(id);
        for (Phase p : filteredPhases) {
           Long phaseid = p.getId();
           List<Task> phaseTasks = getPhaseTasks(phaseid);
           p.setProject(result);
           p.setId(null);
           Phase phaseResult = phaseRepository.save(p);

           for (Task t : phaseTasks) {
               t.setPhase(phaseResult);
               t.setId(null);
               taskRepository.save(t);
           }
        }
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, project.getId().toString()))
            .body(result);
    }


    private List<Phase> getProjectPhases(Long id)  {
        List<Phase> phases = phaseRepository.findAll();
        List<Phase> filteredPhases = new ArrayList<Phase>();
        for (Phase p: phases) {
            if (p.getProject().getId() == id) {
                filteredPhases.add(p);
            }
        }
        return filteredPhases;
    }

    private List<Task> getPhaseTasks(Long id)  {
        List<Task> tasks = taskRepository.findAll();
        List<Task> filteredTasks = new ArrayList<Task>();
        for (Task t: tasks) {
            if (t.getPhase().getId() == id) {
                filteredTasks.add(t);
            }
        }
        return filteredTasks;
    }

    /**
     * GET  /projects : get all the projects.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of projects in body
     */
    @GetMapping("/projects")
    @Timed
    public ResponseEntity<List<Project>> getAllProjects(Pageable pageable) {
        log.debug("REST request to get a page of Projects");
        Page<Project> page = projectRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/projects");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /projects/:id : get the "id" project.
     *
     * @param id the id of the project to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the project, or with status 404 (Not Found)
     */
    @GetMapping("/projects/{id}")
    @Timed
    public ResponseEntity<Project> getProject(@PathVariable Long id) {
        log.debug("REST request to get Project : {}", id);
        Project project = projectRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(project));
    }

    /**
     * GET  /projects/:id : get the "id" project.
     *
     * @param id the id of the project to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the project, or with status 404 (Not Found)
     */
    @GetMapping("/projects/{id}/report")
    @Timed
    public void getProjectReport(@PathVariable Long id, final HttpServletResponse response) throws IOException, JSONException {
        log.debug("REST request to get Project : {}", id);
        Project project = projectRepository.findOne(id);
        String filename = project.getName()+".csv";
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"",
            filename);
        response.setHeader(headerKey, headerValue);
        JSONArray ja = new JSONArray();

        Iterator<Phase> phases = project.getPhases().iterator();
        TOTALS totals = new TOTALS();
        int i=0;
        while (phases.hasNext()) {
            Phase p = phases.next();
            try {
                JSONObject formattedjo = getCSVFormattedPhaseJSONObject(p);
                ja.put(i, formattedjo);
                i++;
                for (Task t: p.getTasks()) {
                    JSONObject taskjo = getCSVFormattedTaskJSONObject(t, totals);
                    ja.put(i, taskjo);
                    i++;
                }
            } catch (JSONException e) {
                log.warn("Failed to unmarshal: {}", p);
                continue;
            }

        }

        // Add newlines before adding totals
        for(int j=0; j<10; j++) {
            JSONObject newline = getCSVFormattedNewlineJSONObject();
            ja.put(i, newline);
            i++;
        }

        // Add PS Totals
        JSONObject psheaderjo = getCSVFormattedPSHeaders();
        ja.put(i, psheaderjo);
        i++;

        JSONObject psjo = getCSVFormattedPSTotals(totals.PS_TOTAL);
        ja.put(i, psjo);
        i++;

        // Add PM Totals
        JSONObject pmjo = getCSVFormattedPMTotals(totals.PM_TOTAL);
        ja.put(i, pmjo);
        i++;

        //log.debug("Returning json array of assets: {}", ja);
        String csv = CDL.toString(ja);
        log.debug("Returning csv: {}", csv);

        if (csv != null) {
            response.getOutputStream().write(csv.getBytes(Charset.forName("UTF-8")));
        }
            ResponseUtil.wrapOrNotFound(Optional.ofNullable(phases));
    }

    private static JSONObject getCSVFormattedPSHeaders() throws JSONException {
        LinkedHashMap<String, String> lhmh = new LinkedHashMap<String, String>();
        lhmh.put("Phase", "");
        lhmh.put("Tasks", "COST");
        lhmh.put("Estimated Hours", "Margin");
        lhmh.put("Resource Skill Level", "Subtotal");
        lhmh.put("COST (Auto populated)", "Risk Markup");
        lhmh.put("Number of Resources", "Total Sell");
        JSONObject  jo = new JSONObject(lhmh);
        return jo;
    }

    private static JSONObject getCSVFormattedPSTotals(Float PS_Total) throws JSONException {
        LinkedHashMap<String, String> lhm = new LinkedHashMap<String, String>();
        lhm.put("Phase", "GW_PS");
        lhm.put("Tasks", "$"+String.valueOf(PS_Total));
        lhm.put("Estimated Hours", "40%");
        Float st = PS_Total + PS_Total *40/100;
        lhm.put("Resource Skill Level", String.valueOf(st));
        lhm.put("COST (Auto populated)", "3%");
        st = st + st*3/100;
        lhm.put("Number of Resources", String.valueOf(st));
        JSONObject  jo = new JSONObject(lhm);
        return jo;
    }

    private static JSONObject getCSVFormattedPMTotals(Float PM_TOTAL) throws JSONException {
        LinkedHashMap<String, String> lhm = new LinkedHashMap<String, String>();
        lhm.put("Phase", "PM");
        lhm.put("Tasks", "$"+String.valueOf(PM_TOTAL));
        lhm.put("Estimated Hours", "40%");
        Float st = PM_TOTAL + PM_TOTAL *40/100;
        lhm.put("Resource Skill Level", String.valueOf(st));
        lhm.put("COST (Auto populated)", "3%");
        st = st + st*3/100;
        lhm.put("Number of Resources", String.valueOf(st));
        JSONObject  jo = new JSONObject(lhm);
        return jo;
    }

    /**
     *
     * @param Phase a
     * @return Json formatted as below
     * OEM	Model	Serial Number	Type	Contract	Name	Address Line 1	City	State	Zip	Primary Contact
     * Phone Number	Email	Start Date	End Date	Coverage Plan
     * Service Vendor	Vendor Primary Contact	Vendor Contact Number	Vendor Email
     * @throws JSONException
     */
    private static JSONObject getCSVFormattedPhaseJSONObject(Phase p) throws JSONException {
        LinkedHashMap<String, String> lhm = new LinkedHashMap<String, String>();

        lhm.put("Phase", p.getName());
        lhm.put("Tasks", "");
        lhm.put("Estimated Hours", getPhaseEstimatedHours(p));
        lhm.put("Resource Skill Level", "");
        lhm.put("COST (Auto populated)", "");
        lhm.put("Number of Resources", "");
        lhm.put("Task Subtotal (COST)","$"+getPhaseSubTotal(p));
        lhm.put("Task Total (w/Margin)", "$"+getPhaseSubTotalWithMargin(p));

        JSONObject  jo = new JSONObject(lhm);
        return jo;
    }

    /**
     * DELETE  /projects/:id : delete the "id" project.
     *
     * @param id the id of the project to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/projects/{id}")
    @Timed
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        log.debug("REST request to delete Project : {}", id);
        projectRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    public static String getPhaseEstimatedHours(Phase p) {
        Float eh = 0.0f;
        for (Task t : p.getTasks()) {
            eh += t.getEstimatedHours();
        }
        return String.valueOf(eh);
    }

    public static String getPhaseSubTotal(Phase p) {
        Float st = 0.0f;
        for (Task t : p.getTasks()) {
            st += t.getSubTotal();
        }
        return String.valueOf(st);
    }

    public static String getPhaseSubTotalWithMargin(Phase p) {
        Float st = 0.0f;
        for (Task t : p.getTasks()) {
            st += t.getSubTotal();
        }
        st += st * 40/100;
        return String.valueOf(st);
    }

    public static JSONObject getCSVFormattedTaskJSONObject(Task t, TOTALS totals) throws JSONException {
        LinkedHashMap<String, String> lhm = new LinkedHashMap<String, String>();
        lhm.put("Phase", "");
        lhm.put("Tasks", t.getName());
        lhm.put("Estimated Hours", String.valueOf(t.getEstimatedHours()));
        lhm.put("Resource Skill Level",  String.valueOf(t.getResource()));
        lhm.put("COST (Auto populated)", "$"+ String.valueOf(t.getCost()));
        lhm.put("Number of Resources",  String.valueOf(t.getNumberOfResources()));
        lhm.put("Task Subtotal (COST)","");
        lhm.put("Task Total (w/Margin)", "");

        if (t.getResource() != null &&  t.getResource().toString().contains("PM")) {
            totals.PM_TOTAL += t.getCost()*t.getEstimatedHours();
        }  else  /*(t.getResource() != null &&  t.getResource().toString().contains("PS")) */ {
            totals.PS_TOTAL += t.getCost()*t.getEstimatedHours();
        }

        return new JSONObject(lhm);
    }

    public static JSONObject getCSVFormattedNewlineJSONObject() throws JSONException {
        LinkedHashMap<String, String> lhm = new LinkedHashMap<String, String>();
        lhm.put("Phase", "");
        lhm.put("Tasks", "");
        lhm.put("Estimated Hours", "");
        lhm.put("Resource Skill Level",  "");
        lhm.put("COST (Auto populated)", "");
        lhm.put("Number of Resources",  "");
        lhm.put("Task Subtotal (COST)","");
        lhm.put("Task Total (w/Margin)", "");
        JSONObject  jo = new JSONObject(lhm);
        return jo;
    }
}
