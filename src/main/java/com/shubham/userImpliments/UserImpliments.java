package com.shubham.userImpliments;

import com.shubham.exception.ResourceNotFoundException;
import com.shubham.model.User;
import com.shubham.repository.UserRepository;
import com.shubham.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.data.mongodb.core.aggregation.StringOperators;
import org.springframework.stereotype.Service;


import java.util.*;
import java.util.stream.Collectors;


@Service
public class UserImpliments implements UserService {

    @Autowired
    private UserRepository userRepository;


    @Override
    public User createUser(User user) {
        User createdUser = userRepository.save(user);
        return createdUser;
    }

    @Override
    public List<User> findAllUser() {
        List<User> allUser = userRepository.findAll();
        return allUser;
    }

    @Override
    public User updateUserByID(String id, User user) {
        User user1 = userRepository.findByUserID(id);
        User user2 = userRepository.findById(user1.get_id()).get();
        if (user2 != null) {
            user2.setUserName(user.getUserName());
            user2.setUserEmail(user.getUserEmail());
            User updatedUser = userRepository.save(user2);
            //User updateedUser1=userRepository.findByUserID(id);

            return updatedUser;
        } else {
            return null;
        }


    }

    @Override
    public Map<String, String> deleteUserByID(String id) throws ResourceNotFoundException {
        User user = userRepository.findByUserID(id);
        if (user != null) {
            userRepository.deleteById(user.get_id());
            Map<String, String> map = new HashMap<>();
            map.put("Status", "200");
            map.put("Response", "User record deleted sucessfully with Given ID" + user.getUserID());
            return map;
        } else {
          throw new ResourceNotFoundException("User record not found for Given ID"+id);
        }


    }

    @Override
    public User findByUserID(String userID) throws ResourceNotFoundException {
        User foundUser = userRepository.findByUserID(userID);
        if (foundUser != null) {
            return foundUser;
        } else {
              throw new ResourceNotFoundException("Data not found for the given ID: " + userID);
        }
//        try {
//            User user1 = userRepository.findByUserID(userID);
//            return user1;
//        } catch (Exception e) {
//            // Log the exception or handle it accordingly
//            System.out.println("An error occurred while fetching user by ID: " + e.getMessage());
//            e.printStackTrace();
//            // Return null or throw a custom exception, depending on your application's requirements
//            return null;
//        }



    }

    public String findStatusMessage(Map<String, Object> callbackRequest) {
        if (callbackRequest.containsKey("callFlowResponse")) {
            Object callFlowResponseObject = callbackRequest.get("callFlowResponse");
            if (callFlowResponseObject instanceof Map) {
                Map<String, Object> callFlowResponseData = (Map<String, Object>) callFlowResponseObject;
                if(callFlowResponseData.containsKey("Messages")) {
                    Object messagesObject = callFlowResponseData.get("Messages");
                    if(messagesObject instanceof String){
                     return (callFlowResponseData.get("Messages") !=null ? callFlowResponseData.get("Messages").toString():"");
                    }else {
                        return (extractMessage(messagesObject, callbackRequest));
                    }
                }
            } else if (callFlowResponseObject instanceof String) {
                return (callbackRequest.get("callFlowResponse") !=null ?callbackRequest.get("callFlowResponse").toString():"");
            }
        } else {
            if (callbackRequest.containsKey("Messages")) {
                Object messagesObject = callbackRequest.get("Messages");
                return (extractMessage(messagesObject, callbackRequest));
            }
        }
        return ("");
    }

    public String extractMessage(Object messagesObject, Map<String, Object> callbackRequest) {
        if (messagesObject instanceof Map) {
            Map<String, Object> messagesMap = (Map<String, Object>) messagesObject;
            if (messagesMap.containsKey("Message")) {
                Object messageArrayObject = messagesMap.get("Message");
                if (messageArrayObject instanceof List) {
                    List<Map<String, Object>> messageList = (List<Map<String, Object>>) messageArrayObject;
                    if (messageList.size() >= 1) {
                        if (messageList.size() == 1) {
                            Map<String, Object> firstMessage = messageList.get(0);
                            if (firstMessage.containsKey("Content")) {
                                String content = firstMessage.get("Content").toString();
                                return(firstMessage.get("Content")!=null ?firstMessage.get("Content").toString():"");
                            }
                        } else if (messageList.size() >= 2) {
                            Map<String, Object> secondMessage = messageList.get(1);
                            if (secondMessage.containsKey("Content")) {
                                return(secondMessage.get("Content")!=null ?secondMessage.get("Content").toString():"");
                            }
                        }
                    }
                } else {

                    if (messageArrayObject instanceof Map) {
                        Map<String, Object> messagesData = (Map<String, Object>) messageArrayObject;
                        return (messagesData.get("Content") !=null ? messagesData.get("Content").toString():"");
                    }
                }
            } else {
                if ((messagesMap.containsKey("More"))) {
                    return (messagesMap.get("More") != null ? messagesMap.get("More").toString() : "");
                }
            }
        } else {
            return (callbackRequest.get("Messages")!=null ? callbackRequest.get("Messages").toString() : "");
        }
        return ("");
    }


    public Map<String, Object> patchComboAttributes(Map<String, Map<String, Object>> comboApplicationAttributes) {
        Map<String, Object> applicationAttributes = new HashMap<>();
        String intMetaKeys = "ordpfrCount";
        Map<String, Set<Object>> metSets = new HashMap<>();
        for (String applicationNumber : comboApplicationAttributes.keySet()) {
            Map<String, Object> appMetaMap = comboApplicationAttributes.get(applicationNumber);
            for (String key : appMetaMap.keySet()) {
                Set<Object> currKeySet = metSets.getOrDefault(key, new HashSet<>());
                currKeySet.add(appMetaMap.get(key));
                metSets.put(key, currKeySet);
            }
        }
        System.out.println(metSets);
        metSets.forEach((key, value) -> {
            if (intMetaKeys.contains(key)) {
                int keyValue = value.stream().map(val -> val.toString().isEmpty() ? 0 : Integer.parseInt(val.toString())).reduce(Integer::sum).orElse(0);
                applicationAttributes.put(key, String.valueOf(keyValue));
            } else if (key.equals("pfrReworkRequired")) {
                applicationAttributes.put(key, value.contains("Y") ? "Y" : "N");
            } else if (key.equals("ordInternalPfrs") || key.equals("rcdInternalPfrs")) {
                Set<String> combinedSet = new HashSet<>();
                value.forEach(val -> combinedSet.addAll((Collection<String>)val));
                applicationAttributes.put(key, String.join(",", combinedSet));
            } else if (key.equals("Readytoreview") || key.equals("medReadyForReview")) {
                applicationAttributes.put(key, value.size() == 1 ? String.join(",", value.stream().map(String::valueOf).collect(Collectors.toSet())):"2");
            } else {
                applicationAttributes.put(key, String.join(",", value.stream().map(String::valueOf).collect(Collectors.toSet())));

            }
        });
        System.out.println(applicationAttributes);
        return applicationAttributes;
    }





}
