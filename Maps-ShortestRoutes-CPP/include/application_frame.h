#ifndef APPLICATION_FRAME_H
#define APPLICATION_FRAME_H
#include "main_frame.h"
#include <wx/msgdlg.h>
#include "rapidxml.hpp"
#include "node.h"

using namespace rapidxml;

class application_frame : public main_frame
{
private:
  void add_node_name(node *, xml_node<> *);
  static long long int get_node_id(xml_node<> *xmlNode);
  static GeographicLib::Math::real get_node_lat(xml_node<> *xmlNode);
  static GeographicLib::Math::real get_node_lon(xml_node<> *xmlNode);
protected:
  void on_parse_click(wxCommandEvent &event) override;
  void on_calculate_click(wxCommandEvent &event) override;
  void on_go_click(wxCommandEvent &event) override;
  void on_file_select(wxFileDirPickerEvent &event) override;
  void on_compute_click(wxCommandEvent &event) override;
public:
  xml_document<> document;
  std::string docContent;
  wxArrayString matches;
  application_frame();
  xml_node<> *rootNode = nullptr;
  wxMessageDialog *invalidInputDialog = new wxMessageDialog(this, "Invalid input", "Error");
};

#endif //APPLICATION_FRAME_H
