package com.fiap.soat12.tc_group_7.controller;

//@SpringBootTest
//@AutoConfigureMockMvc(addFilters = false)
//@DisplayName("ServiceOrderController Unit Tests")
//class ServiceOrderEntityControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @MockitoBean
//    private ServiceOrderService serviceOrderService;
//
//    private ServiceOrderRequestDTO requestDTO;
//    private ServiceOrderResponseDTO responseDTO;
//    private ServiceOrderResponseDTO.VehicleDTO vehicleDTO;
//    private ServiceOrderResponseDTO.CustomerDTO customerDTO;
//    @Autowired
//    private StockService stockService;
//
//    @BeforeEach
//    void setUp() {
//        requestDTO = new ServiceOrderRequestDTO();
//        requestDTO.setCustomerId(1L);
//        requestDTO.setVehicleId(1L);
//
//        responseDTO = new ServiceOrderResponseDTO();
//        responseDTO.setId(1L);
//        responseDTO.setStatus(Status.OPENED);
//        responseDTO.setNotes("Test order");
//    }
//
//    @Nested
//    @DisplayName("Endpoint: POST /api/service-orders")
//    class CreateOrderTests {
//        @Test
//        @DisplayName("Should return 201 Created for a valid request")
//        void createOrder_withValidRequest_shouldReturnCreated() throws Exception {
//            when(serviceOrderService.createServiceOrder(any(ServiceOrderRequestDTO.class))).thenReturn(responseDTO);
//
//            mockMvc.perform(post("/api/service-orders")
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(objectMapper.writeValueAsString(requestDTO)))
//                    .andExpect(status().isCreated())
//                    .andExpect(jsonPath("$.id").value(1L))
//                    .andExpect(jsonPath("$.status").value("OPENED"));
//        }
//
//        @Test
//        @DisplayName("Should return 400 Bad Request if service throws IllegalArgumentException")
//        void createOrder_withInvalidData_shouldReturnBadRequest() throws Exception {
//            when(serviceOrderService.createServiceOrder(any(ServiceOrderRequestDTO.class)))
//                    .thenThrow(new IllegalArgumentException("Invalid input provided"));
//
//            mockMvc.perform(post("/api/service-orders")
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(objectMapper.writeValueAsString(requestDTO)))
//                    .andExpect(status().isBadRequest());
//        }
//    }
//
//    @Nested
//    @DisplayName("Endpoint: PUT /api/service-orders/{id}")
//    class UpdateOrderTests {
//        @Test
//        @DisplayName("Should return 200 OK when updating an existing order")
//        void updateOrder_whenOrderExists_shouldReturnOk() throws Exception {
//            when(serviceOrderService.updateOrder(eq(1L), any(ServiceOrderRequestDTO.class)))
//                    .thenReturn(Optional.of(responseDTO));
//
//            mockMvc.perform(put("/api/service-orders/1")
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(objectMapper.writeValueAsString(requestDTO)))
//                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("$.id").value(1L));
//        }
//
//        @Test
//        @DisplayName("Should return 404 Not Found when trying to update a non-existent order")
//        void updateOrder_whenOrderNotFound_shouldReturnNotFound() throws Exception {
//            when(serviceOrderService.updateOrder(eq(1L), any(ServiceOrderRequestDTO.class)))
//                    .thenReturn(Optional.empty());
//
//            mockMvc.perform(put("/api/service-orders/1")
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(objectMapper.writeValueAsString(requestDTO)))
//                    .andExpect(status().isNotFound());
//        }
//
//        @Test
//        @DisplayName("Should return 400 Bad Request when the service throws an IllegalArgumentException")
//        void updateOrder_whenServiceThrowsError_shouldReturnBadRequest() throws Exception {
//            when(serviceOrderService.updateOrder(eq(1L), any(ServiceOrderRequestDTO.class)))
//                    .thenThrow(new IllegalArgumentException("Invalid data provided for update"));
//
//            mockMvc.perform(put("/api/service-orders/1")
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(objectMapper.writeValueAsString(requestDTO)))
//                    .andExpect(status().isBadRequest());
//        }
//    }
//
//    @Nested
//    @DisplayName("Endpoint: GET /api/service-orders/{id}")
//    class GetOrderByIdTests {
//
//        @Test
//        @DisplayName("Should return 200 OK with the order data when ID exists")
//        void getOrderById_whenOrderExists_shouldReturnOk() throws Exception {
//            when(serviceOrderService.findById(1L)).thenReturn(responseDTO);
//
//            mockMvc.perform(get("/api/service-orders/1"))
//                    .andExpect(status().isOk())
//                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                    .andExpect(jsonPath("$.id", is(1)))
//                    .andExpect(jsonPath("$.status", is("OPENED")))
//                    .andExpect(jsonPath("$.notes", is("Test order")));
//        }
//
//        @Test
//        @DisplayName("Should return 404 Not Found when ID does not exist")
//        void getOrderById_whenOrderDoesNotExist_shouldReturnNotFound() throws Exception {
//            when(serviceOrderService.findById(99L)).thenThrow(new NotFoundException("Ordem de Serviço não encontrada com o ID: 99"));
//
//            mockMvc.perform(get("/api/service-orders/99"))
//                    .andExpect(status().isNotFound());
//        }
//    }
//
//    @Nested
//    @DisplayName("Endpoint: GET /api/service-orders")
//    class GetAllOrdersTests {
//
//        @Test
//        @DisplayName("Should return 200 OK with a list of orders when orders exist")
//        void getAllOrders_whenOrdersExist_shouldReturnOkWithOrderList() throws Exception {
//            ServiceOrderResponseDTO anotherResponseDTO = new ServiceOrderResponseDTO();
//            anotherResponseDTO.setId(2L);
//            anotherResponseDTO.setStatus(Status.APPROVED);
//
//            when(serviceOrderService.findAllOrders()).thenReturn(List.of(responseDTO, anotherResponseDTO));
//
//            mockMvc.perform(get("/api/service-orders"))
//                    .andExpect(status().isOk())
//                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                    .andExpect(jsonPath("$", hasSize(2)))
//                    .andExpect(jsonPath("$[0].id", is(1)))
//                    .andExpect(jsonPath("$[1].id", is(2)));
//        }
//
//        @Test
//        @DisplayName("Should return 200 OK with an empty list when no orders exist")
//        void getAllOrders_whenNoOrdersExist_shouldReturnOkWithEmptyList() throws Exception {
//            when(serviceOrderService.findAllOrders()).thenReturn(Collections.emptyList());
//
//            mockMvc.perform(get("/api/service-orders"))
//                    .andExpect(status().isOk())
//                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                    .andExpect(jsonPath("$", hasSize(0)));
//        }
//    }
//
//    @Nested
//    @DisplayName("Endpoint: DELETE /api/service-orders/{id}")
//    class DeleteOrderTests {
//
//        @Test
//        @DisplayName("Should return 204 No Content when order exists and is logically deleted")
//        void deleteOrder_whenOrderExists_shouldReturnNoContent() throws Exception {
//            when(serviceOrderService.deleteOrderLogically(1L)).thenReturn(true);
//
//            mockMvc.perform(delete("/api/service-orders/1"))
//                    .andExpect(status().isNoContent());
//        }
//
//        @Test
//        @DisplayName("Should return 404 Not Found when order ID does not exist")
//        void deleteOrder_whenOrderDoesNotExist_shouldReturnNotFound() throws Exception {
//            when(serviceOrderService.deleteOrderLogically(99L)).thenReturn(false);
//
//            mockMvc.perform(delete("/api/service-orders/99"))
//                    .andExpect(status().isNotFound());
//        }
//    }
//
//    @Nested
//    @DisplayName("Endpoint: PATCH /api/service-orders/{id}/approve")
//    class ApproveTransitionTests {
//        @Test
//        @DisplayName("Should return 200 OK on a successful approval")
//        void approve_whenSuccessful_shouldReturnOk() throws Exception {
//            ServiceOrderResponseDTO approvedResponse = new ServiceOrderResponseDTO();
//            approvedResponse.setId(1L);
//            approvedResponse.setStatus(Status.APPROVED);
//
//            when(serviceOrderService.approve(eq(1L), any())).thenReturn(Optional.of(approvedResponse));
//
//            mockMvc.perform(patch("/api/service-orders/1/approve").param("employeeId", "1"))
//                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("$.status").value("APPROVED"));
//        }
//
//        @Test
//        @DisplayName("Should return 400 Bad Request for an invalid transition")
//        void approve_whenTransitionIsInvalid_shouldReturnBadRequest() throws Exception {
//            when(serviceOrderService.approve(eq(1L), any()))
//                    .thenThrow(new InvalidTransitionException("Cannot approve from current state"));
//
//            mockMvc.perform(patch("/api/service-orders/1/approve").param("employeeId", "1"))
//                    .andExpect(status().isBadRequest());
//        }
//
//        @Test
//        @DisplayName("Should return 404 Not Found when the order does not exist")
//        void approve_whenOrderNotFound_shouldReturnNotFound() throws Exception {
//            when(serviceOrderService.approve(eq(99L), any())).thenThrow(new NotFoundException("Ordem de Serviço não encontrada com o ID: 99"));
//
//            mockMvc.perform(patch("/api/service-orders/99/approve").param("employeeId", "1"))
//                    .andExpect(status().isNotFound());
//        }
//    }
//
//    @Nested
//    @DisplayName("Endpoint: PATCH /api/service-orders/{id}/diagnose")
//    class DiagnoseTransitionTests {
//
//        @Test
//        @DisplayName("Should return 200 OK on a successful transition to In Diagnosis")
//        void diagnose_whenSuccessful_shouldReturnOk() throws Exception {
//            responseDTO.setStatus(Status.IN_DIAGNOSIS);
//            when(serviceOrderService.diagnose(1L, 10L)).thenReturn(Optional.of(responseDTO));
//
//            mockMvc.perform(patch("/api/service-orders/1/diagnose").param("employeeId", "10"))
//                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("$.status", is("IN_DIAGNOSIS")));
//        }
//
//        @Test
//        @DisplayName("Should return 400 Bad Request for an invalid transition")
//        void diagnose_whenTransitionIsInvalid_shouldReturnBadRequest() throws Exception {
//            when(serviceOrderService.diagnose(1L, 10L))
//                    .thenThrow(new InvalidTransitionException("Cannot start diagnosis from current state"));
//
//            mockMvc.perform(patch("/api/service-orders/1/diagnose").param("employeeId", "10"))
//                    .andExpect(status().isBadRequest());
//        }
//
//        @Test
//        @DisplayName("Should return 404 Not Found when the order does not exist")
//        void diagnose_whenOrderNotFound_shouldReturnNotFound() throws Exception {
//            when(serviceOrderService.diagnose(99L, 10L)).thenThrow(new NotFoundException("Ordem de Serviço não encontrada com o ID: 99"));
//
//            mockMvc.perform(patch("/api/service-orders/99/diagnose").param("employeeId", "10"))
//                    .andExpect(status().isNotFound());
//        }
//    }
//
//    @Nested
//    @DisplayName("Endpoint: PATCH /api/service-orders/{id}/reject")
//    class RejectTransitionTests {
//
//        @Test
//        @DisplayName("Should return 200 OK on a successful rejection")
//        void reject_whenSuccessful_shouldReturnOk() throws Exception {
//            responseDTO.setStatus(Status.REJECTED);
//            when(serviceOrderService.reject(1L, "Too expensive")).thenReturn(Optional.of(responseDTO));
//
//            mockMvc.perform(patch("/api/service-orders/1/reject").param("reason", "Too expensive"))
//                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("$.status", is("REJECTED")));
//        }
//
//        @Test
//        @DisplayName("Should return 400 Bad Request for an invalid transition")
//        void reject_whenTransitionIsInvalid_shouldReturnBadRequest() throws Exception {
//            when(serviceOrderService.reject(1L, "Too expensive"))
//                    .thenThrow(new InvalidTransitionException("Cannot reject from current state"));
//
//            mockMvc.perform(patch("/api/service-orders/1/reject").param("reason", "Too expensive"))
//                    .andExpect(status().isBadRequest());
//        }
//
//        @Test
//        @DisplayName("Should return 404 Not Found when the order does not exist")
//        void reject_whenOrderNotFound_shouldReturnNotFound() throws Exception {
//            when(serviceOrderService.reject(99L, "Too expensive")).thenThrow(new NotFoundException("Ordem de Serviço não encontrada com o ID: 99"));
//
//            mockMvc.perform(patch("/api/service-orders/99/reject").param("reason", "Too expensive"))
//                    .andExpect(status().isNotFound());
//        }
//    }
//
//    @Nested
//    @DisplayName("Endpoint: PATCH /api/service-orders/{id}/wait-for-approval")
//    class WaitForApprovalTransitionTests {
//
//        @Test
//        @DisplayName("Should return 200 OK on a successful transition to Waiting for Approval")
//        void waitForApproval_whenSuccessful_shouldReturnOk() throws Exception {
//            responseDTO.setStatus(Status.WAITING_FOR_APPROVAL);
//            when(serviceOrderService.waitForApproval(1L)).thenReturn(Optional.of(responseDTO));
//
//            mockMvc.perform(patch("/api/service-orders/1/wait-for-approval"))
//                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("$.status", is("WAITING_FOR_APPROVAL")));
//        }
//
//        @Test
//        @DisplayName("Should return 400 Bad Request for an invalid transition")
//        void waitForApproval_whenTransitionIsInvalid_shouldReturnBadRequest() throws Exception {
//            when(serviceOrderService.waitForApproval(1L))
//                    .thenThrow(new InvalidTransitionException("Cannot transition from current state"));
//
//            mockMvc.perform(patch("/api/service-orders/1/wait-for-approval"))
//                    .andExpect(status().isBadRequest());
//        }
//
//        @Test
//        @DisplayName("Should return 404 Not Found when the order does not exist")
//        void waitForApproval_whenOrderNotFound_shouldReturnNotFound() throws Exception {
//            when(serviceOrderService.waitForApproval(99L)).thenThrow(new NotFoundException("Ordem de Serviço não encontrada com o ID: 99"));
//
//            mockMvc.perform(patch("/api/service-orders/99/wait-for-approval"))
//                    .andExpect(status().isNotFound());
//        }
//    }
//
//    @Nested
//    @DisplayName("Endpoint: PATCH /api/service-orders/{id}/finish")
//    class FinishTransitionTests {
//
//        @Test
//        @DisplayName("Should return 200 OK on a successful transition to Finished")
//        void finish_whenSuccessful_shouldReturnOk() throws Exception {
//            responseDTO.setStatus(Status.FINISHED);
//            when(serviceOrderService.finish(1L)).thenReturn(Optional.of(responseDTO));
//
//            mockMvc.perform(patch("/api/service-orders/1/finish"))
//                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("$.status", is("FINISHED")));
//        }
//
//        @Test
//        @DisplayName("Should return 400 Bad Request for an invalid transition")
//        void finish_whenTransitionIsInvalid_shouldReturnBadRequest() throws Exception {
//            when(serviceOrderService.finish(1L))
//                    .thenThrow(new InvalidTransitionException("Cannot finish from current state"));
//
//            mockMvc.perform(patch("/api/service-orders/1/finish"))
//                    .andExpect(status().isBadRequest());
//        }
//
//        @Test
//        @DisplayName("Should return 404 Not Found when the order does not exist")
//        void finish_whenOrderNotFound_shouldReturnNotFound() throws Exception {
//            when(serviceOrderService.finish(99L)).thenThrow(new NotFoundException("Ordem de Serviço não encontrada com o ID: 99"));
//
//            mockMvc.perform(patch("/api/service-orders/99/finish"))
//                    .andExpect(status().isNotFound());
//        }
//    }
//
//    @Nested
//    @DisplayName("Endpoint: PATCH /api/service-orders/{id}/deliver")
//    class DeliverTransitionTests {
//
//        @Test
//        @DisplayName("Should return 200 OK on a successful transition to Delivered")
//        void deliver_whenSuccessful_shouldReturnOk() throws Exception {
//            responseDTO.setStatus(Status.DELIVERED);
//            when(serviceOrderService.deliver(1L)).thenReturn(Optional.of(responseDTO));
//
//            mockMvc.perform(patch("/api/service-orders/1/deliver"))
//                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("$.status", is("DELIVERED")));
//        }
//
//        @Test
//        @DisplayName("Should return 400 Bad Request for an invalid transition")
//        void deliver_whenTransitionIsInvalid_shouldReturnBadRequest() throws Exception {
//            when(serviceOrderService.deliver(1L))
//                    .thenThrow(new InvalidTransitionException("Cannot deliver from current state"));
//
//            mockMvc.perform(patch("/api/service-orders/1/deliver"))
//                    .andExpect(status().isBadRequest());
//        }
//
//        @Test
//        @DisplayName("Should return 404 Not Found when the order does not exist")
//        void deliver_whenOrderNotFound_shouldReturnNotFound() throws Exception {
//            when(serviceOrderService.deliver(99L)).thenThrow(new NotFoundException("Ordem de Serviço não encontrada com o ID: 99"));
//
//            mockMvc.perform(patch("/api/service-orders/99/deliver"))
//                    .andExpect(status().isNotFound());
//        }
//    }
//
//    @Test
//    public void getAverageExecutionTime_withSuccess() throws Exception {
//        AverageExecutionTimeResponseDTO dto = new AverageExecutionTimeResponseDTO(
//                1L,
//                "1 horas, 0 minutos"
//        );
//
//        when(serviceOrderService.calculateAverageExecutionTime(null, null, null))
//                .thenReturn(dto);
//
//        mockMvc.perform(get("/api/service-orders/average-execution-time"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.averageExecutionTimeHours").value(1))
//                .andExpect(jsonPath("$.averageExecutionTimeFormatted").value("1 horas, 0 minutos"));
//    }
//
//    @Nested
//    @DisplayName("Endpoint: PATCH /api/service-orders/{id}/execute")
//    class ExecuteAndWaitOnStockTransitionTests {
//
//        @Test
//        @DisplayName("Should return 200 OK on a successful transition to Delivered")
//        void execute_whenHasStockAvailable_shouldReturnOk() throws Exception {
//            responseDTO.setStatus(Status.IN_EXECUTION);
//            when(serviceOrderService.startOrderExecution(1L)).thenReturn(Optional.of(responseDTO));
//
//            mockMvc.perform(patch("/api/service-orders/1/execute"))
//                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("$.status", is("IN_EXECUTION")));
//        }
//
//        @Test
//        @DisplayName("Should return 400 Bad Request for an invalid transition")
//        void deliver_whenTransitionIsInvalid_shouldReturnBadRequest() throws Exception {
//            when(serviceOrderService.startOrderExecution(1L))
//                    .thenThrow(new InvalidTransitionException("Cannot deliver from current state"));
//
//            mockMvc.perform(patch("/api/service-orders/1/execute"))
//                    .andExpect(status().isBadRequest());
//        }
//
//        @Test
//        @DisplayName("Should return 404 Not Found when the order does not exist")
//        void deliver_whenOrderNotFound_shouldReturnNotFound() throws Exception {
//            when(serviceOrderService.startOrderExecution(99L)).thenThrow(new NotFoundException("Ordem de Serviço não encontrada com o ID: 99"));
//
//            mockMvc.perform(patch("/api/service-orders/99/execute"))
//                    .andExpect(status().isNotFound());
//        }
//    }
//
//    @Nested
//    @DisplayName("Endpoint: GET /api/service-orders/consult")
//    class consultOrderControllerTests {
//
//        @BeforeEach
//        void setUp() {
//            customerDTO = new ServiceOrderResponseDTO.CustomerDTO(1L, "João", "0123456789");
//            vehicleDTO = new ServiceOrderResponseDTO.VehicleDTO(1L, "ABC-1234", "Blazer");
//        }
//
//        @Test
//        @DisplayName("Should return 200 OK with a list of orders when a valid document is provided")
//        void consultOrder_whenDocumentIsValid_thenReturnOk() throws Exception {
//
//            responseDTO.setCustomer(customerDTO);
//            List<ServiceOrderResponseDTO> orderList = Collections.singletonList(responseDTO);
//
//            when(serviceOrderService.findByCustomerInfo("0123456789")).thenReturn(Optional.of(orderList));
//
//            mockMvc.perform(get("/api/service-orders/consult")
//                            .param("document", "0123456789"))
//                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("$[0].id").value("1"))
//                    .andExpect(jsonPath("$[0].customer.name").value("João"));
//        }
//
//        @Test
//        @DisplayName("Should return 200 OK with a single-item list when a valid license plate is provided")
//        void consultOrder_whenLicensePlateIsValid_thenReturnOk() throws Exception {
//
//            responseDTO.setCustomer(customerDTO);
//            responseDTO.setVehicle(vehicleDTO);
//
//            when(serviceOrderService.findByVehicleInfo("ABC-1234")).thenReturn(Optional.of(responseDTO));
//
//            mockMvc.perform(get("/api/service-orders/consult")
//                            .param("licensePlate", "ABC-1234"))
//                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("$[0].id").value("1"))
//                    .andExpect(jsonPath("$[0].customer.name").value("João"))
//                    .andExpect(jsonPath("$[0].vehicle.model").value("Blazer"));
//        }
//
//        @Test
//        @DisplayName("Should return 404 Not Found when document does not exist")
//        void consultOrder_whenDocumentIsNotFound_thenReturnNotFound() throws Exception {
//            String invalidDocument = "00000000000";
//            when(serviceOrderService.findByCustomerInfo(invalidDocument)).thenReturn(Optional.empty());
//
//            mockMvc.perform(get("/api/service-orders/consult")
//                            .param("document", invalidDocument))
//                    .andExpect(status().isNotFound());
//        }
//
//        @Test
//        @DisplayName("Should return 404 Not Found when license plate does not exist")
//        void consultOrder_whenLicensePlateIsNotFound_thenReturnNotFound() throws Exception {
//            String invalidPlate = "XYZ-9876";
//            when(serviceOrderService.findByVehicleInfo(invalidPlate)).thenReturn(Optional.empty());
//
//            mockMvc.perform(get("/api/service-orders/consult")
//                            .param("licensePlate", invalidPlate))
//                    .andExpect(status().isNotFound());
//        }
//
//        @Test
//        @DisplayName("Should return 400 Bad Request when no parameters are provided")
//        void consultOrder_whenNoParametersProvided_thenReturnBadRequest() throws Exception {
//            mockMvc.perform(get("/api/service-orders/consult"))
//                    .andExpect(status().isBadRequest());
//        }
//    }
//}