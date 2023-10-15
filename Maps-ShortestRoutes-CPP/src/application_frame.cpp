#include "application_frame.h"
#include "GeographicLib/Geodesic.hpp"
#include "node.h"
#include <fstream>
#include <sstream>
#include <algorithm>
#include <thread>
#include <string>
#include <wx/valtext.h>
#include <queue>
#include <map>
#include <wx/statbmp.h>
//#include <cpr/cpr.h>

using namespace std;
using namespace GeographicLib;

void application_frame::on_parse_click(wxCommandEvent &event)
{
  try
  {
	std::ifstream osmFile(this->file_picker->GetFileName().GetFullPath().ToStdString());
	std::stringstream buffer;
	buffer << osmFile.rdbuf();
	osmFile.close();
	docContent = buffer.str();
	document.parse<0>(&docContent[0]);
	auto dialog = new wxMessageDialog(this, "File parsed successfully.", "Success");
	dialog->ShowModal();

	rootNode = document.first_node();
	long long int number_of_nodes = 0;
	for (xml_node<> *currentNode = rootNode->first_node("node"); currentNode;
		 currentNode = currentNode->next_sibling("node"))
	{
	  number_of_nodes++;
	}

	long long int number_of_ways = 0;
	for (xml_node<> *currentNode = rootNode->first_node("way"); currentNode;
		 currentNode = currentNode->next_sibling("way"))
	{
	  number_of_ways++;
	}

	nodes_output->WriteText(std::to_string(number_of_nodes));
	ways_output->WriteText(std::to_string(number_of_ways));

	parse_button->Disable();
	calculate_button->Enable();
	compute_button->Enable();
	go_button->Enable();
  } catch (...)
  {
	auto dialog = new wxMessageDialog(this, "There was an error while parsing the file.", "Error");
	dialog->ShowModal();
	parse_button->Disable();
  }
  event.Skip();
}

void application_frame::on_calculate_click(wxCommandEvent &event)
{
  long long int startID = stoll(start_input->GetValue().ToStdString());
  long long int endID = stoll(end_input->GetValue().ToStdString());

  unordered_map<long long int, node *> nodeMap; // storing all the references

  for (xml_node<> *currentNode = rootNode->first_node("node"); currentNode;
	   currentNode = currentNode->next_sibling("node"))
  {
	node *nodeToBeAdded(new node(get_node_id(currentNode),
								 get_node_lat(currentNode),
								 get_node_lon(currentNode)));

	add_node_name(nodeToBeAdded, currentNode);

	nodeMap.insert({nodeToBeAdded->id, nodeToBeAdded});
  }

  for (auto currentWay = rootNode->first_node("way");
	   currentWay;
	   currentWay = currentWay->next_sibling("way"))
  {
	for (auto constituentNode = currentWay->first_node("nd");
		 constituentNode;
		 constituentNode = constituentNode->next_sibling("nd"))
	{
	  if (constituentNode->next_sibling("nd"))
	  {
		nodeMap[get_node_id(constituentNode)]->neighbours
			.push_back(nodeMap[get_node_id(constituentNode->next_sibling("nd"))]);
		nodeMap[get_node_id(constituentNode->next_sibling("nd"))]->neighbours
			.push_back(nodeMap[get_node_id(constituentNode)]);
	  }
	}
  }
  node *startNode = nodeMap[startID];
  node *endNode = nodeMap[endID];

  startNode->dijkstraDistance = 0;

  priority_queue<pair<double, long long int>, vector<pair<double, long long int >>, greater<>>
	  nodeQueue;

  nodeQueue.push({startNode->dijkstraDistance, startNode->id});
  while (!nodeQueue.empty() && !endNode->visited)
  {
	while (nodeMap[nodeQueue.top().second]->visited)
	  nodeQueue.pop();
	node *selectedNode = nodeMap[nodeQueue.top().second];
	nodeQueue.pop();

	assert(selectedNode->visited == false);
	selectedNode->visited = true;

	for (auto neighbour : selectedNode->neighbours)
	{
	  if (!neighbour->visited)
	  {
		double edge_weight;
		node::earth.Inverse(selectedNode->lat, selectedNode->lon, neighbour->lat, neighbour->lon, edge_weight);
		auto newDistance = selectedNode->dijkstraDistance + edge_weight;
		if (newDistance < neighbour->dijkstraDistance)
		{
		  neighbour->dijkstraDistance = newDistance;
		  neighbour->parent = selectedNode;
		  nodeQueue.push({newDistance, neighbour->id});
		}
	  } else
		continue;
	}
  }

  answer_output->Clear(); // clear the output
  // if we could find a path
  if (endNode->visited)
  {
	answer_output->WriteText(std::to_string(endNode->dijkstraDistance / 1000));
  }
  // if we couldn't find a path
  else
  {
	answer_output->WriteText("No path");
  }
  // trace out the path by going through the parents
  if (endNode->visited)
  {
	vector<node *> pathMap;
	startNode->parent = nullptr;
	auto currentNode = endNode;
	while (currentNode != nullptr)
	{
	  pathMap.push_back(currentNode);
	  currentNode = currentNode->parent;
	}
	std::reverse(pathMap.begin(), pathMap.end());
	/*vector<pair<double, double>> latLongPairs;
	for (auto &elem : pathMap)
	{
	  latLongPairs.push_back({elem->lat, elem->lon});
	}
	string pointParam = "";
	for (int i = 1; i <= latLongPairs.size(); i++)
	{
	  pointParam
		  +=
		  to_string(i) + ", " + to_string(latLongPairs[i - 1].first) + ", " + to_string(latLongPairs[i - 1].second)
			  + "|";
	}
	pointParam.pop_back();
	cpr::Response resp = cpr::Get(cpr::Url{"http://open.mapquestapi.com/staticmap/v4/getmap"},
								  cpr::Parameters{
									  {"key", "GzVC1vspsGDSibTQgfXKJZXoYbd7o4H9"},
									  {"size", "600,400"},
									  {"type", "map"},
									  {"imagetype", "png"},
									  {"pois", pointParam}
								  });
	if(resp.status_code == 0)
	  std::cerr << resp.error.message << std::endl;
	ofstream outFile("../response.png");
	outFile << resp.text;
	outFile.close();*/
	cout << "the path is: \n";
	for(auto &elem : pathMap){
	  cout << elem->id << endl;
	}
  }
  for (auto x : nodeMap)
  {
	delete x.second;
  }

  event.Skip();
}

application_frame::application_frame()
	: main_frame(nullptr, wxID_ANY, "Assignment 2")
{
  auto numeric_validator = wxTextValidator(wxFILTER_NUMERIC);
  target_id_input->SetValidator(numeric_validator);
  k_input->SetValidator(numeric_validator);
  start_input->SetValidator(numeric_validator);
  end_input->SetValidator(numeric_validator);

  table->InsertColumn(0, "ID");
  table->InsertColumn(1, "Name");
}

void application_frame::on_go_click(wxCommandEvent &event)
{
  std::string search_query = query_input->GetValue().ToStdString();

  std::transform(search_query.begin(), search_query.end(), search_query.begin(),
				 [](unsigned char c) { return std::tolower(c); });
  //int number_of_named_nodes = 0; // Number of nodes that have a "name" tag associated with them

  matches.clear();
  for (xml_node<> *currentNode = rootNode->first_node(); currentNode; currentNode = currentNode->next_sibling())
  {
	for (xml_node<> *childNode = currentNode->first_node("tag"); childNode;
		 childNode = childNode->next_sibling("tag"))
	{
	  if (strcmp(childNode->first_attribute("k")->value(), "name") == 0)
	  {
		std::string nameValue = childNode->first_attribute("v")->value();
		std::transform(nameValue.begin(), nameValue.end(), nameValue.begin(),
					   [](unsigned char c) { return std::tolower(c); });
		if (nameValue.find(search_query) != std::string::npos)
		{
		  matches.Add(std::string(currentNode->name()) + "-"
						  + std::string(currentNode->first_attribute("id")->value()) + "-"
						  + std::string(childNode->first_attribute("v")->value()));
		}
	  }
	}
  }
  query_results->Clear();
  query_results->Insert(matches, 0);
  event.Skip();
}

void application_frame::on_file_select(wxFileDirPickerEvent &event)
{
  parse_button->Enable();
  event.Skip();
}

void application_frame::on_compute_click(wxCommandEvent &event)
{
  long long target_node_id = stoll(target_id_input->GetValue().ToStdString());
  xml_node<> *targetNode = nullptr;
  for (xml_node<> *currentNode = rootNode->first_node("node"); currentNode;
	   currentNode = currentNode->next_sibling("node"))
  {
	if (stoll(currentNode->first_attribute("id")->value()) == target_node_id)
	{
	  targetNode = currentNode;
	  break;
	}
  }
  if (targetNode == nullptr)
  {
	(new wxMessageDialog(this, "Invalid target ID.", "Error"))->ShowModal();
	event.Skip();
	return;
  }
  Math::real targetLatitude = stod(targetNode->first_attribute("lat")->value());
  Math::real targetLongitude = stod(targetNode->first_attribute("lon")->value());
  long long int targetID = stoll(targetNode->first_attribute("id")->value());
  node target{targetID, targetLatitude, targetLongitude};

  int numberOfClosestNeighbours = stoi(k_input->GetValue().ToStdString());

  auto comparisonLambda = [target](const node &a, const node &b) {
	return a.distance(target) < b.distance(target);
  };

  priority_queue<node, std::vector<node>, decltype(comparisonLambda)> kNeighbours(comparisonLambda);

  xml_node<> *currentNode = rootNode->first_node("node");
  while (currentNode && kNeighbours.size() < numberOfClosestNeighbours)
  {
	long long int currentID = stoll(currentNode->first_attribute("id")->value());
	if (currentID != targetID)
	{
	  Math::real currentLatitude = stod(currentNode->first_attribute("lat")->value());
	  Math::real currentLongitude = stod(currentNode->first_attribute("lon")->value());
	  node nodeToBeAdded{currentID, currentLatitude, currentLongitude};
	  for (auto childNode = currentNode->first_node("tag");
		   childNode;
		   childNode = childNode->next_sibling("tag"))
	  {
		if (strcmp(childNode->first_attribute("k")->value(), "name") == 0)
		{
		  nodeToBeAdded.name = childNode->first_attribute("v")->value();
		  break;
		}
	  }
	  kNeighbours.push(nodeToBeAdded);
	}
	currentNode = currentNode->next_sibling("node");
  }

  while (currentNode)
  {
	long long int currentID = stoll(currentNode->first_attribute("id")->value());
	if (currentID != targetID)
	{
	  Math::real currentLatitude = stod(currentNode->first_attribute("lat")->value());
	  Math::real currentLongitude = stod(currentNode->first_attribute("lon")->value());
	  node nodeToBeAdded{currentID, currentLatitude, currentLongitude};
	  for (auto childNode = currentNode->first_node("tag");
		   childNode;
		   childNode = childNode->next_sibling("tag"))
	  {
		if (strcmp(childNode->first_attribute("k")->value(), "name") == 0)
		{
		  nodeToBeAdded.name = childNode->first_attribute("v")->value();
		  break;
		}
	  }
	  kNeighbours.push(nodeToBeAdded);
	  kNeighbours.pop();
	}
	currentNode = currentNode->next_sibling("node");
  }
  assert(kNeighbours.size() <= numberOfClosestNeighbours);

  long long int index = 0;
  while (!kNeighbours.empty())
  {
	auto returnValue = table->InsertItem(index, to_string(kNeighbours.top().id));

	if (!kNeighbours.top().name.empty())
	{
	  table->SetItem(returnValue, 1, kNeighbours.top().name);
	}
	index++;
	kNeighbours.pop();
  }

  event.Skip();
}

void application_frame::add_node_name(node *actualNode, xml_node<> *xmlNode)
{
  for (xml_node<> *childXmlNode = xmlNode->first_node("tag"); childXmlNode;
	   childXmlNode = childXmlNode->next_sibling("tag"))
  {
	if (strcmp(childXmlNode->first_attribute("k")->value(), "name") == 0)
	{
	  actualNode->name = childXmlNode->first_attribute("k")->value();
	  break;
	}
  }
}

long long int application_frame::get_node_id(xml_node<> *xmlNode)
{
  if (strcmp(xmlNode->name(), "node") == 0)
	return std::stoll(xmlNode->first_attribute("id")->value());
  else if (strcmp(xmlNode->name(), "nd") == 0)
	return std::stoll(xmlNode->first_attribute("ref")->value());
  else
	{
		assert(1 == 0);
		return -1;
	}
}

GeographicLib::Math::real application_frame::get_node_lat(xml_node<> *xmlNode)
{
  return std::stod(xmlNode->first_attribute("lat")->value());
}

GeographicLib::Math::real application_frame::get_node_lon(xml_node<> *xmlNode)
{
  return std::stod(xmlNode->first_attribute("lon")->value());
}
