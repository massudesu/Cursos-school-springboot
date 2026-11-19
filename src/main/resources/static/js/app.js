/*
 * EducaHub - Front-end da aplicação.
 * Configure API_BASE de acordo com o endereço da sua API.
 * Contrato esperado: /api/alunos, /api/professores, /api/cursos,
 * /api/escolas, /api/turmas e /api/salas.
 */
const API_BASE = "http://localhost:8080/api";

const entityConfig = {
  alunos: { title:"Alunos", description:"Gerencie os estudantes da instituição.", endpoint:"alunos",
    fields:[["nome","Nome completo","text",true],["email","E-mail","email",true],["telefone","Telefone","tel",false],["dataNascimento","Data de nascimento","date",false],["matricula","Matrícula","text",true],["escolaId","ID da escola","number",true]] },
  professores: { title:"Professores", description:"Gerencie os profissionais responsáveis pelas aulas.", endpoint:"professores",
    fields:[["nome","Nome completo","text",true],["email","E-mail","email",true],["telefone","Telefone","tel",false],["especialidade","Especialidade","text",true],["escolaId","ID da escola","number",true]] },
  cursos: { title:"Cursos", description:"Cadastre os cursos oferecidos pela instituição.", endpoint:"cursos",
    fields:[["nome","Nome do curso","text",true],["descricao","Descrição","textarea",false],["cargaHoraria","Carga horária (horas)","number",true],["escolaId","ID da escola","number",true]] },
  escolas: { title:"Escolas", description:"Mantenha os dados das instituições responsáveis.", endpoint:"escolas",
    fields:[["nome","Nome da escola","text",true],["cnpj","CNPJ","text",false],["email","E-mail institucional","email",true],["telefone","Telefone","tel",false],["endereco","Endereço","text",true]] },
  turmas: { title:"Turmas", description:"Organize as turmas e seus vínculos acadêmicos.", endpoint:"turmas",
    fields:[["nome","Nome/identificação da turma","text",true],["anoLetivo","Ano letivo","number",true],["turno","Turno","select",true,["Matutino","Vespertino","Noturno","Integral"]],["cursoId","Curso","select",true,"cursos"],["professorId","Professor","select",true,"professores"],["salaId","Sala","select",true,"salas"]] },
  salas: { title:"Salas", description:"Gerencie os espaços físicos utilizados nas aulas.", endpoint:"salas",
    fields:[["nome","Nome/número da sala","text",true],["capacidade","Capacidade de alunos","number",true],["localizacao","Localização","text",false],["escolaId","ID da escola","number",true]] }
};

const state = { entity:"alunos", records:[], editingId:null };
const $ = (selector) => document.querySelector(selector);
const tableHead = $("#tableHead"), tableBody = $("#tableBody");

function getId(record) { return record.id ?? record.idAluno ?? record.idProfessor ?? record.idCurso ?? record.idEscola ?? record.idTurma ?? record.idSala; }
function showStatus(message="") { $("#statusMessage").textContent = message; }
function toast(message) {
  const el=$("#toast"); el.textContent=message; el.classList.add("show");
  window.clearTimeout(toast.timer); toast.timer=window.setTimeout(()=>el.classList.remove("show"),2600);
}
function humanize(key) {
  return key.replace(/([A-Z])/g," $1").replace(/^./,c=>c.toUpperCase()).replace(/Id$/,"ID");
}
function displayValue(value) {
  if(value===null || value===undefined || value==="") return "—";
  if(typeof value==="object") return JSON.stringify(value);
  return String(value);
}
function renderTable() {
  const config=entityConfig[state.entity];
  const search=$("#searchInput").value.trim().toLowerCase();
  const rows=state.records.filter(record=>JSON.stringify(record).toLowerCase().includes(search));
  const columns=config.fields.slice(0,4).map(field=>field[0]);
  tableHead.innerHTML=`<tr>${columns.map(key=>`<th>${humanize(key)}</th>`).join("")}<th>AÇÕES</th></tr>`;
  if(!rows.length) {
    tableBody.innerHTML=`<tr><td class="empty-cell" colspan="${columns.length+1}">${state.records.length ? "Nenhum registro corresponde à busca." : "Nenhum registro cadastrado ainda."}</td></tr>`;
  } else {
    tableBody.innerHTML=rows.map(record=>{
      const id=getId(record);
      return `<tr>${columns.map(key=>`<td>${escapeHTML(displayValue(record[key]))}</td>`).join("")}<td><div class="row-actions"><button class="action-btn" data-action="edit" data-id="${escapeHTML(id)}">Editar</button><button class="action-btn delete" data-action="delete" data-id="${escapeHTML(id)}">Excluir</button></div></td></tr>`;
    }).join("");
  }
  $("#recordCount").textContent=`${rows.length} registro(s)`;
  updateHeroCounts();
}
function escapeHTML(value) {
  return String(value).replace(/[&<>"']/g,char=>({"&":"&amp;","<":"&lt;",">":"&gt;",'"':"&quot;","'":"&#39;"}[char]));
}
async function apiRequest(path, options={}) {
  const response=await fetch(`${API_BASE}${path}`,{
    ...options,
    headers:{"Content-Type":"application/json",...(options.headers||{})}
  });
  const raw=await response.text();
  let data=null;
  try { data=raw ? JSON.parse(raw) : null; } catch { data=raw; }
  if(!response.ok) {
    const message=data?.message || data?.mensagem || (typeof data==="string" ? data : `Erro HTTP ${response.status}`);
    throw new Error(message);
  }
  return data;
}
async function loadRecords() {
  const config=entityConfig[state.entity];
  showStatus("");
  tableBody.innerHTML=`<tr><td class="loading-cell" colspan="5">Carregando informações...</td></tr>`;
  try {
    const data=await apiRequest(`/${config.endpoint}`);
    state.records=Array.isArray(data) ? data : (data?.content || data?.dados || []);
    renderTable();
  } catch(error) {
    state.records=[]; renderTable();
    showStatus(`Não foi possível carregar os dados. ${error.message}. Verifique se a API está em execução e se o endereço está correto.`);
  }
}
function selectEntity(entity) {
  if(!entityConfig[entity]) return;
  state.entity=entity; state.editingId=null;
  document.querySelectorAll(".nav-item").forEach(button=>button.classList.toggle("active",button.dataset.entity===entity));
  $("#entityTitle").textContent=entityConfig[entity].title;
  $("#entityDescription").textContent=entityConfig[entity].description;
  $("#searchInput").value="";
  loadRecords();
}
async function openForm(record=null) {
  state.editingId=record ? getId(record) : null;
  const config=entityConfig[state.entity];
  $("#dialogTitle").textContent=record ? `Editar ${config.title.toLowerCase().slice(0,-1)}` : `Novo cadastro`;
  $("#formError").textContent="";
  const lookupData = {};
  if(state.entity==="turmas") {
    try {
      const [cursos, professores, salas] = await Promise.all([
        apiRequest("/cursos"), apiRequest("/professores"), apiRequest("/salas")
      ]);
      lookupData.cursos = cursos;
      lookupData.professores = professores;
      lookupData.salas = salas;
    } catch(error) {
      $("#formError").textContent = "Não foi possível carregar cursos, professores e salas. " + error.message;
      return;
    }
  }
  $("#formFields").innerHTML=config.fields.map(([key,label,type,required,options])=>{
    const value=record?.[key] ?? "";
    const requiredAttr=required ? "required" : "";
    let control="";
    if(type==="textarea") control=`<textarea id="field-${key}" name="${key}" ${requiredAttr}>${escapeHTML(value)}</textarea>`;
    else if(type==="select" && Array.isArray(options)) control=`<select id="field-${key}" name="${key}" ${requiredAttr}><option value="">Selecione...</option>${options.map(option=>`<option ${value===option?"selected":""}>${option}</option>`).join("")}</select>`;
    else if(type==="select") {
      const items=lookupData[options] || [];
      const labelFor=item=>options==="cursos" ? `${item.nome} (ID ${getId(item)})` : options==="professores" ? `${item.nome} (ID ${getId(item)})` : `${item.nome} (ID ${getId(item)})`;
      control=`<select id="field-${key}" name="${key}" ${requiredAttr}><option value="">Selecione...</option>${items.map(item=>`<option value="${getId(item)}" ${String(value)===String(getId(item))?"selected":""}>${escapeHTML(labelFor(item))}</option>`).join("")}</select>`;
    }
    else control=`<input id="field-${key}" name="${key}" type="${type}" value="${escapeHTML(value)}" ${requiredAttr} ${type==="number"?'min="0"':""}>`;
    return `<div class="field ${type==="textarea"?"full":""}"><label for="field-${key}">${label}${required?' *':''}</label>${control}</div>`;
  }).join("");
  $("#recordDialog").showModal();
}
function closeForm() { $("#recordDialog").close(); state.editingId=null; }
async function saveRecord(event) {
  event.preventDefault();
  const form=new FormData($("#recordForm"));
  const payload={};
  for(const [key,value] of form.entries()) {
    const field=entityConfig[state.entity].fields.find(item=>item[0]===key);
    payload[key]=field?.[2]==="number" ? Number(value) : (field?.[2]==="date" && value==="" ? null : value.trim());
  }
  const config=entityConfig[state.entity];
  const isEditing=state.editingId!==null && state.editingId!==undefined;
  const path=`/${config.endpoint}${isEditing?`/${encodeURIComponent(state.editingId)}`:""}`;
  $("#saveButton").disabled=true; $("#saveButton").textContent="Salvando...";
  try {
    await apiRequest(path,{method:isEditing?"PUT":"POST",body:JSON.stringify(payload)});
    closeForm(); await loadRecords(); toast(isEditing?"Cadastro atualizado com sucesso!":"Cadastro realizado com sucesso!");
  } catch(error) {
    $("#formError").textContent=`Não foi possível salvar: ${error.message}`;
  } finally {
    $("#saveButton").disabled=false; $("#saveButton").textContent="Salvar cadastro";
  }
}
async function deleteRecord(id) {
  const record=state.records.find(item=>String(getId(item))===String(id));
  if(!confirm(`Deseja realmente excluir "${record?.nome || "este registro"}"?`)) return;
  try {
    await apiRequest(`/${entityConfig[state.entity].endpoint}/${encodeURIComponent(id)}`,{method:"DELETE"});
    await loadRecords(); toast("Registro excluído com sucesso!");
  } catch(error) { showStatus(`Não foi possível excluir o registro: ${error.message}`); }
}
function updateHeroCounts() {
  if(state.entity==="alunos") {
    $("#heroStudentCount").textContent=state.records.length.toString().padStart(2,"0");
    $("#previewAlunos").textContent=state.records.length.toString().padStart(2,"0");
  }
  if(state.entity==="cursos") $("#previewCursos").textContent=state.records.length.toString().padStart(2,"0");
}

async function loadAcademicEnrollments() {
  const schoolSelect=$("#filterSchool"), courseSelect=$("#filterCourse"), teacherSelect=$("#filterTeacher");
  const result=$("#enrollmentResults");
  result.innerHTML='<p class="loading-cell">Carregando matrículas...</p>';
  try {
    const [matriculas, escolas, cursos, professores, alunos, turmas] = await Promise.all([
      apiRequest("/matriculas"), apiRequest("/escolas"), apiRequest("/cursos"),
      apiRequest("/professores"), apiRequest("/alunos"), apiRequest("/turmas")
    ]);
    const studentSelect=$("#newEnrollmentStudent"), classSelect=$("#newEnrollmentClass");
    const oldStudent=studentSelect.value, oldClass=classSelect.value;
    studentSelect.innerHTML='<option value="">Selecione um aluno</option>'+alunos.map(item=>`<option value="${getId(item)}">${escapeHTML(item.nome)} — ${escapeHTML(item.matricula||"sem matrícula")}</option>`).join("");
    classSelect.innerHTML='<option value="">Selecione uma turma</option>'+turmas.map(item=>`<option value="${getId(item)}">${escapeHTML(item.nome)} — ${escapeHTML(item.turno||"")}</option>`).join("");
    studentSelect.value=oldStudent; classSelect.value=oldClass;
    const fill=(select,items,label)=> {
      const previous=select.value;
      select.innerHTML='<option value="">Todas</option>'+items.map(item=>`<option value="${getId(item)}">${escapeHTML(label(item))}</option>`).join("");
      if([...select.options].some(option=>option.value===previous)) select.value=previous;
    };
    fill(schoolSelect,escolas,item=>item.nome);
    fill(courseSelect,cursos,item=>item.nome);
    fill(teacherSelect,professores,item=>item.nome);

    const school=schoolSelect.value, course=courseSelect.value, teacher=teacherSelect.value;
    const filtered=matriculas.filter(item=>
      (!school || String(item.escolaId)===school) &&
      (!course || String(item.cursoId)===course) &&
      (!teacher || String(item.professorId)===teacher)
    );
    if(!filtered.length) {
      result.innerHTML='<div class="enrollment-empty">Nenhuma matrícula encontrada para os filtros selecionados.</div>';
      return;
    }
    result.innerHTML=`<div class="enrollment-table-wrap"><table class="enrollment-table"><thead><tr><th>ALUNO</th><th>MATRÍCULA</th><th>CURSO</th><th>ESCOLA</th><th>PROFESSOR</th><th>TURMA</th><th>AÇÕES</th></tr></thead><tbody>${filtered.map(item=>`<tr><td><strong>${escapeHTML(item.alunoNome)}</strong></td><td>${escapeHTML(item.matricula||"—")}</td><td>${escapeHTML(item.cursoNome)}</td><td>${escapeHTML(item.escolaNome)}</td><td>${escapeHTML(item.professorNome)}</td><td>${escapeHTML(item.turmaNome)}</td><td><button class="action-btn delete" data-enrollment-delete="${item.id}">Excluir</button></td></tr>`).join("")}</tbody></table></div><p class="enrollment-count">${filtered.length} aluno(s) encontrado(s)</p>`;
  } catch(error) {
    result.innerHTML=`<p class="enrollment-error">Não foi possível carregar as matrículas. ${escapeHTML(error.message)}. Verifique se a API está ativa.</p>`;
  }
}

function init() {
  $("#entityNav").addEventListener("click",event=>{
    const button=event.target.closest("[data-entity]"); if(button) selectEntity(button.dataset.entity);
  });
  tableBody.addEventListener("click",event=>{
    const button=event.target.closest("[data-action]"); if(!button) return;
    const record=state.records.find(item=>String(getId(item))===button.dataset.id);
    if(button.dataset.action==="edit" && record) openForm(record);
    if(button.dataset.action==="delete") deleteRecord(button.dataset.id);
  });
  $("#addButton").addEventListener("click",()=>openForm());
  $("#refreshButton").addEventListener("click",loadRecords);
  ["filterSchool","filterCourse","filterTeacher"].forEach(id=>$("#"+id).addEventListener("change",loadAcademicEnrollments));
  $("#refreshEnrollments").addEventListener("click",loadAcademicEnrollments);
  $("#enrollmentResults").addEventListener("click", async event => {
    const button = event.target.closest("[data-enrollment-delete]");
    if (!button) return;
    if (!confirm("Deseja realmente excluir esta matrícula?")) return;
    try {
      await apiRequest(`/matriculas/${encodeURIComponent(button.dataset.enrollmentDelete)}`, {method:"DELETE"});
      await loadAcademicEnrollments();
      toast("Matrícula excluída com sucesso!");
    } catch(error) {
      $("#enrollmentFormMessage").textContent = "Não foi possível excluir a matrícula: " + error.message;
    }
  });
  $("#createEnrollment").addEventListener("click",async()=>{
    const alunoId=$("#newEnrollmentStudent").value, turmaId=$("#newEnrollmentClass").value;
    const message=$("#enrollmentFormMessage");
    if(!alunoId || !turmaId) { message.textContent="Selecione um aluno e uma turma."; return; }
    message.textContent="Salvando matrícula...";
    try {
      await apiRequest("/matriculas",{method:"POST",body:JSON.stringify({alunoId:Number(alunoId),turmaId:Number(turmaId),status:"Ativa"})});
      message.textContent="Aluno matriculado com sucesso!";
      $("#newEnrollmentStudent").value=""; $("#newEnrollmentClass").value="";
      await loadAcademicEnrollments();
    } catch(error) { message.textContent="Não foi possível matricular: "+error.message; }
  });
  $("#searchInput").addEventListener("input",renderTable);
  $("#recordForm").addEventListener("submit",saveRecord);
  $("#closeDialog").addEventListener("click",closeForm);
  $("#cancelButton").addEventListener("click",closeForm);
  $("#menuToggle").addEventListener("click",()=>{
    const nav=$("#mainNav"),open=nav.classList.toggle("open");
    $("#menuToggle").setAttribute("aria-expanded",String(open));
  });
  $("#mainNav").addEventListener("click",event=>{if(event.target.closest("a"))$("#mainNav").classList.remove("open");});
  selectEntity("alunos");
  loadAcademicEnrollments();
}
document.addEventListener("DOMContentLoaded",init);
