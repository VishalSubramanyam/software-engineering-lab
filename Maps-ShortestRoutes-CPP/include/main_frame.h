///////////////////////////////////////////////////////////////////////////
// C++ code generated with wxFormBuilder (version 3.10.1-0-g8feb16b)
// http://www.wxformbuilder.org/
//
// PLEASE DO *NOT* EDIT THIS FILE!
///////////////////////////////////////////////////////////////////////////

#pragma once

#include <wx/artprov.h>
#include <wx/xrc/xmlres.h>
#include <wx/string.h>
#include <wx/stattext.h>
#include <wx/gdicmn.h>
#include <wx/font.h>
#include <wx/colour.h>
#include <wx/settings.h>
#include <wx/filepicker.h>
#include <wx/button.h>
#include <wx/bitmap.h>
#include <wx/image.h>
#include <wx/icon.h>
#include <wx/sizer.h>
#include <wx/textctrl.h>
#include <wx/listbox.h>
#include <wx/listctrl.h>
#include <wx/frame.h>

///////////////////////////////////////////////////////////////////////////


///////////////////////////////////////////////////////////////////////////////
/// Class main_frame
///////////////////////////////////////////////////////////////////////////////
class main_frame : public wxFrame
{
	private:

	protected:
		wxStaticText* file_label;
		wxFilePickerCtrl* file_picker;
		wxButton* parse_button;
		wxStaticText* nodes_label;
		wxTextCtrl* nodes_output;
		wxStaticText* ways_label;
		wxTextCtrl* ways_output;
		wxStaticText* query_label;
		wxTextCtrl* query_input;
		wxButton* go_button;
		wxListBox* query_results;
		wxStaticText* target_node_label;
		wxTextCtrl* target_id_input;
		wxStaticText* k_label;
		wxTextCtrl* k_input;
		wxButton* compute_button;
		wxListCtrl* table;
		wxStaticText* start_label;
		wxTextCtrl* start_input;
		wxStaticText* end_label;
		wxTextCtrl* end_input;
		wxButton* calculate_button;
		wxStaticText* answer_label;
		wxTextCtrl* answer_output;
		wxBoxSizer* mapSizer;

		// Virtual event handlers, override them in your derived class
		virtual void on_file_select( wxFileDirPickerEvent& event ) { event.Skip(); }
		virtual void on_parse_click( wxCommandEvent& event ) { event.Skip(); }
		virtual void on_go_click( wxCommandEvent& event ) { event.Skip(); }
		virtual void on_compute_click( wxCommandEvent& event ) { event.Skip(); }
		virtual void on_calculate_click( wxCommandEvent& event ) { event.Skip(); }


	public:

		main_frame( wxWindow* parent, wxWindowID id = wxID_ANY, const wxString& title = wxT("Assignment 2"), const wxPoint& pos = wxDefaultPosition, const wxSize& size = wxSize( 541,620 ), long style = wxCLOSE_BOX|wxMINIMIZE_BOX|wxTAB_TRAVERSAL );

		~main_frame();

};

